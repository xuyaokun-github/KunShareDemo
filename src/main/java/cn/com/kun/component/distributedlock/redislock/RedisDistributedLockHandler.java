package cn.com.kun.component.distributedlock.redislock;

import cn.com.kun.component.distributedlock.DistributedLockHandler;
import cn.com.kun.controller.redisson.RedissonDemoController;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Redistemplate方式实现redis分布式锁
 * 实现了锁的自动过期与续约,上锁时无需再指定锁定时间
 *
 * TODO：
 * 1.实现锁的可重入
 * 2.实现可同时上多把锁（不推荐玩得这么复杂，应该拆分方法，逻辑拆分）
 *
 * author:xuyaokun_kzx
 * date:2021/7/13
 * desc:
*/
@Component
public class RedisDistributedLockHandler implements DistributedLockHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedissonDemoController.class);

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    private ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<String>();

    private HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);

    private static final ConcurrentMap<String, Timeout> TIMEOUT_MAP = new ConcurrentHashMap<>();

    @Override
    public boolean lock(String resourceName) {

        //requestId可以放到ThreadLocal中
        String requestId = UUID.randomUUID().toString();
        for (;;){
            //目前不支持超时时间，后续扩展（若不支持超时，后续可能这把锁无法自动过期）
            boolean isGetLock = redisLockUtil.getLock(resourceName, requestId, 35);
            if (isGetLock) {

                //获取到锁之后，把requestId放入threadlocal，后续解锁需要使用
                requestIdThreadLocal.set(requestId);

                //启动一个续约锁的watch dog任务
                startLockRenewWatchDog(resourceName, requestId);
                break;
            }else {
                //没抢到锁，先自旋(注意这里的自旋，对于集群环境来说没什么问题，但是假如是单节点，有可能会造成线程饥饿)
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
        return false;
    }

    private void startLockRenewWatchDog(String resourceName, String requestId) {

        if (LOGGER.isDebugEnabled()){
//            LOGGER.debug("启动续约锁task：{}， requestId:{}", resourceName, requestId);
        }
        //这里创建出来的Timeout必须保存起来,后续可以在提前解锁时释放该任务，这样可以避免内存泄漏
        Timeout timeout = hashedWheelTimer.newTimeout(new LockRenewTimeTask(resourceName, requestId), 30, TimeUnit.SECONDS);
        // 存放的逻辑（参考Redisson）
        TIMEOUT_MAP.put(requestId, timeout);

    }

    class LockRenewTimeTask implements TimerTask{

        private String resourceName;

        private String requestId;

        public LockRenewTimeTask(String resourceName, String requestId) {
            this.resourceName = resourceName;
            this.requestId = requestId;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            //将会在30秒后执行
            //先判断redis中是否仍有该锁
            try {
//                String value = (String) redisTemplate.opsForValue().get(resourceName);
//                Object value = redisTemplate.opsForValue().get(resourceName);

                byte[] value = (byte[]) redisTemplate.execute((RedisCallback<byte[]>) connection ->
                        connection.get(resourceName.getBytes()));
                if (value != null && requestId.equals(new String((byte[]) value,"UTF-8"))){
                    if (LOGGER.isDebugEnabled()){
//                        LOGGER.debug("开始续约锁：{}， requestId:{}", resourceName, requestId);
                    }
                    //假如锁的值仍等于当前线程设置的值，说明持有锁的线程未发生变化，则续约锁
                    redisTemplate.expire(resourceName, 35, TimeUnit.SECONDS);
                    //然后启动一个新的TimeTask
//                hashedWheelTimer.newTimeout(new LockRenewTimeTask(resourceName, requestId), 30, TimeUnit.SECONDS);
                    hashedWheelTimer.newTimeout(this, 30, TimeUnit.SECONDS);
                }else {
                    //否则退出
                    if (LOGGER.isDebugEnabled()){
//                        LOGGER.debug("锁已失效或被正常释放，无需续约,锁名：{}", resourceName);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 解锁的时候，必须释放时间轮任务
     * @param resourceName
     * @return
     */
    @Override
    public boolean unlock(String resourceName) {

        //从ThreadLocal中拿requestId
        String requestId = requestIdThreadLocal.get();
        //释放时间轮任务
        Timeout timeout = TIMEOUT_MAP.get(requestId);
        if (timeout != null){
            if (!timeout.isExpired() && !timeout.isCancelled()){
                if (LOGGER.isDebugEnabled()){
//                    LOGGER.debug("释放时间轮任务：{}", requestId);
                }
                timeout.cancel();
            }
        }
        //解锁
        return redisLockUtil.releaseLock(resourceName, requestId);
    }

}
