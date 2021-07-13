package cn.com.kun.component.clusterlock.redissonlock;

import cn.com.kun.component.clusterlock.ClusterLockHandler;
import org.springframework.stereotype.Component;

@Component
public class RedissonLockHandler implements ClusterLockHandler {

    @Override
    public boolean lockPessimistic(String resourceName) {


        return false;
    }

    @Override
    public boolean unlockPessimistic(String resourceName) {

        return false;
    }

    @Override
    public void lockOptimism(String resourceName) {
        throw new RuntimeException("RedissonLockHandler暂不支持乐观锁");
    }

    @Override
    public void unlockOptimism() {
        throw new RuntimeException("RedissonLockHandler暂不支持乐观锁");
    }

}
