package cn.com.kun.service.distributedlock;

import cn.com.kun.component.distributedlock.dblock.version1.DBDistributedLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBClusterLockDemoService3 {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBClusterLockDemoService3.class);

    @Autowired
    DBClusterLockDemoService2 dbClusterLockDemoService2;

    @Autowired
    DBDistributedLockHandler dbClusterLockHandler;

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
        dbClusterLockHandler.lock(resourceName);
        dbClusterLockDemoService2.testWithAnno();
        dbClusterLockHandler.unlock(resourceName);

    }

//    @DBClusterLock()
//    @DBClusterLock(resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno2")

    /**
        验证是否到底是锁行还是锁表？
        先用一个线程抢A锁，然后执行一个10秒的操作，然后释放锁
        同时用一个线程抢B锁，看是否能抢到锁，假如不能说明是锁表。
        我们要的目标是锁行。

        实践证明是行锁。
     */
    public void testReentrantLock3(){

        String resourceName1 = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno";
        String resourceName2 = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno2";
        new Thread(()->{
            dbClusterLockHandler.lock(resourceName1);
            LOGGER.info("我是线程1，已抢到锁");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("我是线程1，释放锁");
            dbClusterLockHandler.unlock(resourceName1);
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            dbClusterLockHandler.lock(resourceName2);
            LOGGER.info("我是线程2，已抢到锁");
            LOGGER.info("我是线程2，释放锁");
            dbClusterLockHandler.unlock(resourceName2);
        }).start();

    }

}
