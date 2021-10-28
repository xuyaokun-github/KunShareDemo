package cn.com.kun.component.clusterlock.redissonlock;

import cn.com.kun.component.clusterlock.ClusterLockHandler;
import org.springframework.stereotype.Component;

@Component
public class RedissonLockHandler implements ClusterLockHandler {

    @Override
    public boolean lock(String resourceName) {


        return false;
    }

    @Override
    public boolean unlock(String resourceName) {

        return false;
    }

}
