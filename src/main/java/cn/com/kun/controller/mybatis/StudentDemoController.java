package cn.com.kun.controller.mybatis;

import cn.com.kun.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RequestMapping("/student-demo")
@RestController
public class StudentDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(StudentDemoController.class);

    @Autowired
    StudentService studentService;

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
}
