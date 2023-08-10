package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import cn.com.kun.bean.entity.Student;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.service.StudentService;
import cn.com.kun.service.mybatis.DeadLockDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 验证Batch死锁问题
 *
 * author:xuyaokun_kzx
 * date:2022/6/22
 * desc:
*/
@RequestMapping("/deadlock-demo")
@RestController
public class DeadLockDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeadLockDemoController.class);

    @Autowired
    DeadLockDemoService deadLockDemoService;

    @Autowired
    BasicBatchConfigurer basicBatchConfigurer;

    @Autowired
    StudentService studentService;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("mySecondJob")
    Job mySecondJob;

    /**
     * 这个方法能重现BATCH_JOB_INSTANCE表死锁问题
     * @return
     */
    @GetMapping("/testInsertMore")
    public String testInsertMore(){

        //清空表数据
        deadLockDemoService.deleteAll();

        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            new Thread(()->{
                DeadLockDemoDO deadLockDemoDO = new DeadLockDemoDO();
                deadLockDemoDO.setId(Long.valueOf(finalI));
                deadLockDemoDO.setVersion("1");
                deadLockDemoDO.setDemoName("kunghsu");
                deadLockDemoDO.setDemoKey(UUID.randomUUID().toString());
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //先select，后update就会出现死锁
                deadLockDemoService.insert(deadLockDemoDO);

                //这个可以重现死锁问题
//                JobParameters jobParameters = new JobParametersBuilder()
//                        .addLong("time", System.currentTimeMillis())
//                        .addString("index", "" + finalI)
//                        .toJobParameters();
//                basicBatchConfigurer.getJobRepository().createJobInstance("kunghsu", jobParameters);

            }).start();
        }
        return "OK";
    }


    /**
     * 死锁问题实验（没复现）
     *
     * 做实验的时候，设置idCard列为普通索引
     * @return
     */
    @GetMapping("/testDeadlockProblem")
    public String testDeadlockProblem(){

        //先插入100条测试数据
        String idCard = "aa" + DateUtils.toStr(new Date(), "HHmm");
        String idCard2 = idCard + "1";

        idCard = "XKHDYZ";
        idCard2 = "XKHDYZ1";

        Student student = new Student();
        student.setIdCard(idCard2);
        student.setAddress(UUID.randomUUID().toString());
        student.setStudentName("kunghsu");
        student.setCreateTime(new Date());
        for (int i = 0; i < 5000; i++) {
            int res = studentService.save(student);
        }

        CountDownLatch countDownLatch = new CountDownLatch(8);

        //一个线程做更新，一个线程做插入，插入100次
        String finalIdCard2 = idCard2;
        String finalIdCard = idCard;
        new Thread(()->{
            try {
                countDownLatch.countDown();
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadUtils.sleep(1000);
            long start = System.currentTimeMillis();
            LOGGER.info("更新线程开始");
            while (true){
                int res = studentService.updateByIdCard(UUID.randomUUID().toString(), finalIdCard2);

                int res2 = studentService.updateByIdCard(UUID.randomUUID().toString(), finalIdCard);
                if (System.currentTimeMillis() - start > 10 * 1000){
                    break;
                }
            }
            LOGGER.info("更新线程退出");
        }, "update-thread").start();

        //构造测试数据
        Student student1 = new Student();
        student1.setIdCard(idCard);
        student1.setAddress(UUID.randomUUID().toString());
        student1.setStudentName("kunghsu");
        student1.setCreateTime(new Date());
        Student student2 = new Student();
        student2.setIdCard(idCard2);
        student2.setAddress(UUID.randomUUID().toString());
        student2.setStudentName("kunghsu");
        student2.setCreateTime(new Date());
        String idCard3 = idCard+"3";
        Student student3 = new Student();
        student3.setIdCard(idCard3);
        student3.setAddress(UUID.randomUUID().toString());
        student3.setStudentName("kunghsu");
        student3.setCreateTime(new Date());
        String idCard4 = idCard+"0";
        Student student4 = new Student();
        student4.setIdCard(idCard4);
        student4.setAddress(UUID.randomUUID().toString());
        student4.setStudentName("kunghsu");
        student4.setCreateTime(new Date());
        for (int i = 0; i < 8; i++) {
            new Thread(()->{
                try {
                    countDownLatch.countDown();
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<Student> studentList = new ArrayList<>();
                studentList.add(student3);
                studentList.add(student4);
                for (int j = 0; j < 100; j++) {
                    Student otherStudent = new Student();
                    otherStudent.setIdCard(UUID.randomUUID().toString());
                    otherStudent.setAddress(UUID.randomUUID().toString());
                    otherStudent.setStudentName("kunghsu");
                    otherStudent.setCreateTime(new Date());
                }

                for (int k = 0; k < 1000; k++) {
                    if(k % 2 ==0){
                        studentList.add(student1);
                    }else {
                        studentList.add(student2);
                    }
                }

                LOGGER.info("插入线程开始 " + Thread.currentThread().getName());
                for (int j = 0; j < 20; j++) {
//                    studentList.stream().parallel().forEach(obj->{
//                        int res = studentService.save(obj);
//                    });

                    studentService.saveBatch(studentList);
                }

                //这样无法复现死锁
//                for (int j = 0; j < 10000; j++) {
//                    int res = studentService.save(student1);
//                }

                LOGGER.info("插入线程退出 " + Thread.currentThread().getName());
            }, "insert-thread" + i).start();
        }




        return "OK";
    }

    /**
     * 配合batch做测试
     *
     * @return
     */
    @GetMapping("/testDeadlockProblem2")
    public String testDeadlockProblem2() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        //先插入100条测试数据
        String idCard = "aa" + DateUtils.toStr(new Date(), "HHmm");
        String idCard2 = idCard + "1";

        idCard = "XKHDYZ";
        idCard2 = "XKHDYZ1";

        Student student = new Student();
        student.setIdCard(idCard2);
        student.setAddress(UUID.randomUUID().toString());
        student.setStudentName("kunghsu");
        student.setCreateTime(new Date());
        for (int i = 0; i < 5000; i++) {
            int res = studentService.save(student);
        }

        //一个线程做更新，一个线程做插入，插入100次
        String finalIdCard = idCard2;
        new Thread(()->{
            ThreadUtils.sleep(100);
            long start = System.currentTimeMillis();
            LOGGER.info("更新线程开始");
            while (true){
                int res = studentService.updateByIdCard(UUID.randomUUID().toString(), finalIdCard);
                if (System.currentTimeMillis() - start > 20 * 1000){
                    break;
                }
            }
            LOGGER.info("更新线程退出");
        }, "update-thread").start();

        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(mySecondJob, jobParameters);

        return "OK";
    }

    /**
     * 可以复现死锁问题：
     * 死锁问题排查7--一个事务做多个update一个事务做多个insert
     *
     * @return
     */
    @GetMapping("/testDeadlockProblem3")
    public String testDeadlockProblem3(){

        //先插入100条测试数据
        String idCard = "XKHDYZ";
        String idCard2 = "XKHDYZ1";

        Student student = new Student();
        student.setIdCard(idCard);
        student.setAddress(UUID.randomUUID().toString());
        student.setStudentName("kunghsu");
        student.setCreateTime(new Date());
        Student student2 = new Student();
        student2.setIdCard(idCard2);
        student2.setAddress(UUID.randomUUID().toString());
        student2.setStudentName("kunghsu");
        student2.setCreateTime(new Date());
        for (int i = 0; i < 500; i++) {
            int res = studentService.save(student);
        }

        CountDownLatch countDownLatch = new CountDownLatch(2);

        //一个线程做更新，一个线程做插入，插入100次
        String finalIdCard = idCard;
        String finalIdCard2 = idCard2;
        new Thread(()->{

            try {
                countDownLatch.countDown();
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            deadLockDemoService.updateByIdCard(finalIdCard, finalIdCard2);
            LOGGER.info("更新线程退出");
        }, "update-thread").start();

        new Thread(()->{
            try {
                countDownLatch.countDown();
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            deadLockDemoService.saveBatch(student, student2);

            LOGGER.info("插入线程退出 " + Thread.currentThread().getName());

        }, "insert-thread").start();

        return "OK";
    }
}
