package cn.com.kun.service.distributedlock;

import cn.com.kun.component.distributedlock.dblock.version2.DBLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBLockDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBLockDemoService.class);

    @Autowired
    DBLockHandler dbClusterLockHandler;

    public void test() throws InterruptedException {
        String resourceName = "cn.com.kun.service.distributedlock.DBLockDemoService.test";

        while (true){
            //上锁
            if (dbClusterLockHandler.lock(resourceName)){
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
                Thread.sleep(3000);
            }
        }
    }

    /**
     * 验证是否支持自动续约
     * @throws InterruptedException
     */
    public void testRunLongTimeJob() throws InterruptedException {

        String resourceName = "cn.com.kun.service.distributedlock.DBLockDemoService.test";
        //上锁
        while (true){
            if (dbClusterLockHandler.lock(resourceName)){
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
                Thread.sleep(3000);
            }
        }

    }
}
