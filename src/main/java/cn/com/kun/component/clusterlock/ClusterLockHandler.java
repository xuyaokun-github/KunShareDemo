package cn.com.kun.component.clusterlock;

/**
 * 分布式锁处理器接口
 *
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
public interface ClusterLockHandler {

    boolean lockPessimistic(String resourceName);

    boolean unlockPessimistic(String resourceName);

    /**
     * 乐观锁-上锁
     */
    void lockOptimism(String resourceName);

    /**
     * 乐观锁-解锁
     */
    void unlockOptimism();


}
