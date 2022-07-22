package cn.com.kun.component.distributedlock.redissonlock;

import cn.com.kun.component.distributedlock.DistributedLockHandler;
import org.springframework.stereotype.Component;

/**
 * TODO
 * Created by xuyaokun On 2022/7/21 22:56
 * @desc:
 */
@Component
public class RedissonLockHandler implements DistributedLockHandler {

    @Override
    public boolean lock(String resourceName) {


        return false;
    }

    @Override
    public boolean unlock(String resourceName) {

        return false;
    }

}
