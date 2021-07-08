package cn.com.kun.service.clusterlock;

import cn.com.kun.component.clusterlock.dblock.DBClusterLockHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBClusterLockDemoService {

    public final static Logger LOGGER = LoggerFactory.getLogger(DBClusterLockDemoService.class);

    @Autowired
    DBClusterLockHandler dbClusterLockHandler;

//    @Transactional
    public void test(){
        String resourceName = "cn.com.kun.service.clusterlock.DBClusterLockService.test";
        //上锁
        dbClusterLockHandler.lockPessimistic(resourceName);
        LOGGER.info("i am DBClusterLockDemoService 开始执行任务,当前线程：{}", Thread.currentThread().getName());
        try {
//            Thread.sleep(60000);//模拟一个耗时任务
            Thread.sleep(10);//
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("i am DBClusterLockDemoService 任务结束,当前线程：{}", Thread.currentThread().getName());
        //解锁
        dbClusterLockHandler.unlockPessimistic(resourceName);
    }


}
