package cn.com.kun.service.distributedlock.dblock;

import cn.com.kun.component.distributedlock.dblock.DBLock;
import cn.com.kun.component.distributedlock.dblock.DistributedDbLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBLockDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBLockDemoService.class);

    String resourceName = "cn.com.kun.service.distributedlock.dblock.DBLockDemoService.test";

    @Autowired
    DistributedDbLock dbClusterLockHandler;

    public void test() throws InterruptedException {

        while (true){
            //上锁
            if (dbClusterLockHandler.tryLock(resourceName)){
                LOGGER.info("i am DBLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
                try {
//            Thread.sleep(60000);//模拟一个耗时任务
                    Thread.sleep(2000);//
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("i am DBLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
                //解锁
                dbClusterLockHandler.unlock(resourceName);
                break;
            }else {
                //没抢到锁，先休眠
                Thread.sleep(100);
            }
        }
    }

    /**
     * 验证是否支持自动续约
     * @throws InterruptedException
     */
    public void testRunLongTimeJob() throws InterruptedException {

        String resourceName = "cn.com.kun.service.distributedlock.dblock.DBLockDemoService.test";
        //上锁
        while (true){
            if (dbClusterLockHandler.tryLock(resourceName)){
                LOGGER.info("i am DBClusterLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
                try {
                    Thread.sleep(60000 + 10000);//模拟一个耗时任务
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("i am DBClusterLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
                //解锁
                dbClusterLockHandler.unlock(resourceName);
                break;
            }else {
                //没抢到锁，先休眠
                Thread.sleep(100);
            }
        }

    }

    public void testByLockMethod() {

        dbClusterLockHandler.lock(resourceName);
        try {
            LOGGER.info("i am DBLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
            Thread.sleep(2000);//
            LOGGER.info("i am DBLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            dbClusterLockHandler.unlock(resourceName);
        }
    }

    public void testRunLongTimeJobByLockMethod() {

        dbClusterLockHandler.lock(resourceName);
        try {
            LOGGER.info("i am DBLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
            Thread.sleep(60000 + 10000);//模拟一个耗时任务
            LOGGER.info("i am DBLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            dbClusterLockHandler.unlock(resourceName);
        }
    }

    /**
     * 注解方式
     */
    @DBLock(resourceName = "cn.com.kun.service.distributedlock.dblock.DBLockDemoService.test")
    public void testRunFastJobByAnnotation() {
        LOGGER.info("i am DBLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
        try {
            Thread.sleep(2000);//
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("i am DBLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
    }

    @DBLock(resourceName = "cn.com.kun.service.distributedlock.dblock.DBLockDemoService.test")
    public void testRunLongTimeJobByAnnotation() {
        LOGGER.info("i am DBLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
        try {
            Thread.sleep(60000 + 10000);//模拟一个耗时任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("i am DBLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
    }

}
