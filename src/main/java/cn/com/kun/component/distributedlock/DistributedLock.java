package cn.com.kun.component.distributedlock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 * Created by xuyaokun On 2022/7/21 23:01
 * @desc:
 */
public interface DistributedLock {

    /**
     * Acquires the lock.
     *
     */
    void lock(String resourceName);

    /**
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     *
     */
    void lockInterruptibly(String resourceName) throws InterruptedException;

    /**
     * Acquires the lock only if it is free at the time of invocation.
     *
     */
    boolean tryLock(String resourceName);

    /**
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     *
     */
    boolean tryLock(String resourceName, long time, TimeUnit unit) throws InterruptedException;

    /**
     * Releases the lock.
     *
     */
    void unlock(String resourceName);

}

