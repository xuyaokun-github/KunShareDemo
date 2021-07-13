package cn.com.kun.component.clusterlock.redislock;

import cn.com.kun.component.clusterlock.ClusterLockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Redistemplate方式实现redis分布式锁
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
public class RedisClusterLockHandler implements ClusterLockHandler {

    @Autowired
    RedisLockUtil redisLockUtil;

//    private ThreadLocal<LinkedList<LockWrapper>> lockWrapperThreadLocal = new ThreadLocal<LinkedList<LockWrapper>>();
    private ThreadLocal<String> requestIdThreadLocal = new ThreadLocal<String>();

    @Override
    public boolean lockPessimistic(String resourceName) {

        //requestId可以放到ThreadLocal中
        String requestId = UUID.randomUUID().toString();
        for (;;){
            //目前不支持超时时间，后续扩展（若不支持超时，后续可能这把锁无法自动过期）
            boolean isGetLock = redisLockUtil.getLock(resourceName, requestId, 0);
            if (isGetLock) {
                requestIdThreadLocal.set(requestId);
                break;
            }else {
                //没抢到锁，先自旋
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean unlockPessimistic(String resourceName) {
        //从ThreadLocal中拿requestId
        return redisLockUtil.releaseLock(resourceName, requestIdThreadLocal.get());
    }

    @Override
    public void lockOptimism(String resourseName) {

    }

    @Override
    public void unlockOptimism() {

    }
}
