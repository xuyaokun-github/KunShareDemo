package cn.com.kun.service.distributedlock.dblockVersion1;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.component.distributedlock.dblockVersion1.DBClusterLock;
import cn.com.kun.component.distributedlock.dblockVersion1.DBDistributedLockHandler;
import cn.com.kun.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBClusterLockDemoService2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBClusterLockDemoService2.class);

    @Autowired
    DBDistributedLockHandler dbClusterLockHandler;

    @Autowired
    StudentMapper studentMapper;

    public void test2(){
        String resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.test2";
        //上锁
        dbClusterLockHandler.lock(resourceName);
        LOGGER.info("i am DBClusterLockDemoService2 任务2开始,当前线程：{}", Thread.currentThread().getName());
        try {
            Thread.sleep(10);//模拟一个耗时任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("i am DBClusterLockDemoService2 任务2结束,当前线程：{}", Thread.currentThread().getName());
        //解锁
        dbClusterLockHandler.unlock(resourceName);
    }


    /**
     * 支持同时上多把锁
     *
     */
    @DBClusterLock(resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno")
    public void testWithMutilLock(){
        String resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.test2";
        //上锁
        dbClusterLockHandler.lock(resourceName);
        LOGGER.info("i am DBClusterLockDemoService2 任务2开始,当前线程：{}", Thread.currentThread().getName());
        try {
            Thread.sleep(10);//模拟一个耗时任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("i am DBClusterLockDemoService2 任务2结束,当前线程：{}", Thread.currentThread().getName());
        //解锁
        dbClusterLockHandler.unlock(resourceName);
    }

    @DBClusterLock(resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno")
    public void testWithAnno(){
        LOGGER.info("i am DBClusterLockDemoService2 任务2开始,当前线程：{}", Thread.currentThread().getName());
        try {
            Thread.sleep(10);//模拟一个耗时任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //假如这里，有操作数据库，涉及到其他事务，会不会受影响？

        LOGGER.info("i am DBClusterLockDemoService2 任务2结束,当前线程：{}", Thread.currentThread().getName());
    }

    @DBClusterLock(resourceName = "cn.com.kun.service.clusterlock.DBClusterLockDemoService2.testWithAnno2")
    public void testWithAnno2(){
        LOGGER.info("i am DBClusterLockDemoService2 任务2开始,当前线程：{}", Thread.currentThread().getName());
        try {
            Thread.sleep(10);//模拟一个耗时任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //假如这里，有操作数据库，涉及到其他事务，会不会受影响？不会
//        Student student = new Student();
//        student.setStudentName("123456" + System.currentTimeMillis());
//        int res = studentMapper.insert(student);
        Student student = new Student();
        student.setId(11L);
        student.setStudentName("123456" + System.currentTimeMillis());
        int res = studentMapper.update(student);
        LOGGER.info("i am DBClusterLockDemoService2 任务2结束,当前线程：{}", Thread.currentThread().getName());
    }

}
