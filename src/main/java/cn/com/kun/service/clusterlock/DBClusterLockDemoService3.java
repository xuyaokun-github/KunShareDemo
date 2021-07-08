package cn.com.kun.service.clusterlock;

import cn.com.kun.component.clusterlock.dblock.DBClusterLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBClusterLockDemoService3 {

    public final static Logger LOGGER = LoggerFactory.getLogger(DBClusterLockDemoService3.class);

    @Autowired
    DBClusterLockDemoService2 dbClusterLockDemoService2;

    @Autowired
    DBClusterLockHandler dbClusterLockHandler;

    /**
     * 验证锁是否可重入
     */
    public void testReentrantLock(){
        for (int i = 0; i < 5; i++) {
            /*
            testWithAnno是一个上了锁的方法，现在多次调用它，验证是否只需要抢锁一次
            实现锁的可重入，避免同一个线程对同一个锁反复抢占
            这种情况，还是会反复抢锁，因为一次方法调用，结束时是必然需要解锁的。
            这个例子体现不出可重入特性
             */
            dbClusterLockDemoService2.testWithAnno();
        }
    }


    /**
     * 验证锁是否可重入
     */
    public void testReentrantLock2(){
            /*
            体现可重入特性
             */
        String resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno";
        dbClusterLockHandler.lockPessimistic(resourceName);
        dbClusterLockDemoService2.testWithAnno();
        dbClusterLockHandler.unlockPessimistic(resourceName);

    }



}
