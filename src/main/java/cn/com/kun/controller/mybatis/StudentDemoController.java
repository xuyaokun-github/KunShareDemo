package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@RequestMapping("/student-demo")
@RestController
public class StudentDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(StudentDemoController.class);

    @Autowired
    StudentService studentService;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("mySecondJob")
    Job mySecondJob;

    @GetMapping("/testUpdate")
    public String testUpdate(){

        Student student = new Student();
        student.setIdCard(UUID.randomUUID().toString());
        student.setAddress(UUID.randomUUID().toString());
        student.setStudentName("kunghsu");
        student.setCreateTime(new Date());
        int res = studentService.save(student);
        return "OK";
    }

    /**
     *
     * @return
     */
    @GetMapping("/testUniqueIndex")
    public String testUniqueIndex(){

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {

            new Thread(()->{

                try {
                    countDownLatch.countDown();
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                studentService.saveIfNotExist();
            }).start();
        }

        return "OK";
    }

    /**
     *
     * @return
     */
    @GetMapping("/testUniqueKey")
    public String testUniqueKey(){

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {

            new Thread(()->{

                try {
                    countDownLatch.countDown();
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                studentService.saveIfNotExist2();
            }).start();
        }

        return "OK";
    }

    /**
     * 测试获取锁超时问题
     *
     * @return
     */
    @GetMapping("/testLockProblem2")
    public String testLockProblem2(){

        //先插入100条测试数据
        String idCard = "aa" + DateUtils.toStr(new Date(), "HHmm");
        Student student = new Student();
        student.setIdCard(idCard);
        student.setAddress(UUID.randomUUID().toString());
        student.setStudentName("kunghsu");
        student.setCreateTime(new Date());
        for (int i = 0; i < 100; i++) {
            int res = studentService.save(student);
        }

        String idCard4 = idCard + "2";

        CountDownLatch countDownLatch = new CountDownLatch(6);

        //一个线程做更新，一个线程做插入，插入100次
        new Thread(()->{
            try {
                countDownLatch.countDown();
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadUtils.sleep(1000);
            LOGGER.info("更新线程开始");
            long start = System.currentTimeMillis();
            /*
                假如更新条件没命中，会锁全表，会导致插入操作报 获取锁超时异常
                结果是一个插入操作都没执行成功，全部失败了，因为更新操作一直抢占着整张表
             */
            studentService.updateByIdCard2(UUID.randomUUID().toString(), idCard4);
            //假如更新条件命中，命中索引，和插入操作抢的锁互不干扰，插入操作正常
//            studentService.updateByIdCard2(UUID.randomUUID().toString(), idCard);
            LOGGER.info("更新线程退出,耗时：{}ms", System.currentTimeMillis() - start);
        }).start();

        String idCard2 = idCard + "1";
        String idCard3 = idCard;
        Student student2 = new Student();
        student2.setIdCard(idCard2);
        student2.setAddress(UUID.randomUUID().toString());
        student2.setStudentName("kunghsu");
        student2.setCreateTime(new Date());
        Student student3 = new Student();
        student3.setIdCard(idCard3);
        student3.setAddress(UUID.randomUUID().toString());
        student3.setStudentName("kunghsu");
        student3.setCreateTime(new Date());

        for (int i = 0; i < 16; i++) {
            int finalI = i;
            new Thread(()->{

                try {
                    countDownLatch.countDown();
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Student student1 = new Student();
                student1.setIdCard(idCard + finalI);
                student1.setAddress(UUID.randomUUID().toString());
                student1.setStudentName("kunghsu");
                student1.setCreateTime(new Date());
                int res = studentService.save2(student1);
                LOGGER.info("插入线程退出");
            }).start();
        }

        return "OK";
    }

}
