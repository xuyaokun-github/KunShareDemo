package cn.com.kun.component.distributedlock.dblock;

import cn.com.kun.component.distributedlock.DistributedLock;
import cn.com.kun.component.distributedlock.dblock.dao.DbLockDaoDelagate;
import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

//import java.sql.Date;

/**
 * 分布式数据库锁
 *
 * author:xuyaokun_kzx
 * date:2022/4/9
 * desc:
*/
@Component
public class DistributedDbLock implements DistributedLock {

    private final static Logger LOGGER = LoggerFactory.getLogger(DistributedDbLock.class);

    @Autowired
    private ApplicationContext applicationContext;

//    @Autowired
//    private DbLockMapper dbLockMapper;

    @Autowired
    private DbLockDaoDelagate dbLockMapper;

    private ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<String>();

    /**
     * 时间轮
     */
    private HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);

    private static final ConcurrentMap<String, Timeout> TIMEOUT_MAP = new ConcurrentHashMap<>();

    @Override
    public void lockInterruptibly(String resourceName) throws InterruptedException {

        //TODO 支持中断

    }

    @Transactional
    @Override
    public boolean tryLock(String resourceName) {

        //查询锁的参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("resource", resourceName);
        String currentThreadName = Thread.currentThread().getName();

        //获取requestId
        //重入时通过ThreadLocal拿到自己的requestId
        String requestId = requestIdThreadLocal.get();
        if (StringUtils.isEmpty(requestId)){
            requestId = UUID.randomUUID().toString();
            requestIdThreadLocal.set(requestId);
        }

        //抢到锁就返回成功
        DbLockDO dbLockDO = dbLockMapper.acquireLock(paramMap);
        if (dbLockDO == null){
            //说明锁记录还没插入
            LOGGER.info("抢锁失败，DbLockDO为空，请检查数据库记录行。resource：{}, 当前线程{}", resourceName, currentThreadName);
            return false;
        }else {
            boolean getLockFlag = false;
            //校验是否和自己的放入的requestId一样
            if (requestId.equals(dbLockDO.getRequestId()) || StringUtils.isEmpty(dbLockDO.getRequestId())){
                //假如为空说明已经被抢占
                getLockFlag = true;
//                LOGGER.info("锁未被抢占，抢锁成功。resource：{}, 当前线程{}", resourceName, currentThreadName);
            }else {
                //假如没命中，则判断时间是否已经超过了1分钟，假如超过了，则强制抢锁
                Date requestTime = dbLockDO.getRequestTime();
                Date now = new Date();
                if (now.getTime() - 1 * 60 * 1000L > requestTime.getTime()){
                    getLockFlag = true;
                }
            }

            if (getLockFlag){
                //更新DB
                dbLockDO.setRequestId(requestId);
                dbLockDO.setRequestTime(new java.util.Date());
//                dbLockDO.setRequestTime(new Date(new java.util.Date().getTime()));
                int res = dbLockMapper.updateRequestInfo(dbLockDO);
                if (res > 0){
                    //抢锁成功，启动时间轮续锁
                    startLockRenewWatchDog(resourceName, requestId);
//                    LOGGER.info("抢锁成功启动时间轮。resource：{}, 当前线程{}", resourceName, currentThreadName);
                }else {
                    //更新数据库失败
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean tryLock(String resourceName, long time, TimeUnit unit) throws InterruptedException {

        long millis = unit.toMillis(time);
        long start = System.currentTimeMillis();
        while (true){
            if (tryLock(resourceName)){
                return true;
            }
            if (System.currentTimeMillis() - start > millis){
                return false;
            }
        }
    }

    /**
     * 上锁（阻塞性）
     * 这里加事务注解，有可能事务会超时，抛出“等锁超时”异常使主流程结束
     * 这里为了保证阻塞性，无加事务注解的必要，若选择了加事务注解则必须try住“等锁超时”异常
     */
//    @Transactional
    @Override
    public void lock(String resourceName) {

        while (true){
            //这里相当于this调用，等于没有开启事务，所以必须拿到代理对象进行调用
            boolean getLock = getProxy().tryLock(resourceName);
            if (getLock){
                //一直抢，直到抢到为止
                break;
            }else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //
                    LOGGER.error("DBLockHandler lock InterruptedException");
                }
            }
        }
    }

    private DistributedLock getProxy() {

        return applicationContext.getBean(DistributedDbLock.class);
    }


    private void startLockRenewWatchDog(String resourceName, String requestId) {

        if (LOGGER.isDebugEnabled()){
//            LOGGER.debug("启动续约锁task：{}， requestId:{}", resourceName, requestId);
        }
        //这里创建出来的Timeout必须保存起来,后续可以在提前解锁时释放该任务，这样可以避免内存泄漏
        Timeout timeout = hashedWheelTimer.newTimeout(new DistributedDbLock.LockRenewTimeTask(resourceName, requestId), 30, TimeUnit.SECONDS);
        // 存放的逻辑（参考Redisson）
        TIMEOUT_MAP.put(requestId, timeout);

    }


    class LockRenewTimeTask implements TimerTask {

        private String resourceName;

        private String requestId;

        public LockRenewTimeTask(String resourceName, String requestId) {
            this.resourceName = resourceName;
            this.requestId = requestId;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            //将会在30秒后执行
            try {
                //判断db中是否仍有该锁
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("resource", resourceName);
                paramMap.put("requestId", requestId);

                DbLockDO dbLockDO = dbLockMapper.selectLock(paramMap);
                if (dbLockDO != null){
                    if (LOGGER.isDebugEnabled()){
//                        LOGGER.debug("开始续约锁：{}， requestId:{}", resourceName, requestId);
                    }
                    //假如锁的值仍等于当前线程设置的值，说明持有锁的线程未发生变化，则续约锁
                    //更新DB
                    dbLockDO.setRequestTime(new java.util.Date());
//                    dbLockDO.setRequestTime(new Date(new java.util.Date().getTime()));
                    int res = dbLockMapper.updateRequestInfo(dbLockDO);
                    if(res > 0){
                        //然后启动一个新的TimeTask
                        hashedWheelTimer.newTimeout(this, 30, TimeUnit.SECONDS);
                    }else {
                        //更新DB失败，续锁失败
                        LOGGER.warn("锁续约失败，resource：{} requestId:{}", resourceName, requestId);
                    }

                }else {
                    //否则退出
                    if (LOGGER.isDebugEnabled()){
//                        LOGGER.debug("锁已失效或被正常释放，无需续约,锁名：{}", resourceName);
                    }
                }
            }catch (Exception e){
                LOGGER.error("续锁异常", e);
            }

        }
    }

    @Override
    public void unlock(String resourceName) {

        //将requestId重新置成空，表示未被抢占
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
            //必须移除时间轮任务对象
            TIMEOUT_MAP.remove(requestId);
        }

        DbLockDO dbLockDO = new DbLockDO();
        dbLockDO.setRequestId(requestId);
        dbLockDO.setResource(resourceName);
        int res = dbLockMapper.resetRequestInfo(dbLockDO);
        if (res == 0){
            LOGGER.info("解锁失败，resourceName：{} requestId:{}", resourceName, requestId);
        }
        requestIdThreadLocal.remove();
    }


}
