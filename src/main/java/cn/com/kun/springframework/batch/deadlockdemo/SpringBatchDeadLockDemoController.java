package cn.com.kun.springframework.batch.deadlockdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * 验证Batch死锁问题
 *
 * author:xuyaokun_kzx
 * date:2022/6/22
 * desc:
*/
@RequestMapping("/springbatch-deadlock-demo")
@RestController
public class SpringBatchDeadLockDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringBatchDeadLockDemoController.class);

    @Autowired
    SpringBatchDeadLockDemoService springBatchDeadLockDemoService;

    @GetMapping("/testInsertMore2")
    public String testInsertMore2(){

        //清空表数据
        int maxId = springBatchDeadLockDemoService.selectMaxJobInstanceId();

        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            maxId = maxId + 1;
            int finalMaxId = maxId;
            new Thread(()->{
                Map<String, Object> map = new HashMap<>();
                map.put("JOB_INSTANCE_ID", finalMaxId);
                map.put("JOB_NAME", "kunghsu");
                map.put("JOB_KEY", UUID.randomUUID().toString().replaceAll("-", ""));
                map.put("VERSION", 0);

                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                springBatchDeadLockDemoService.insertBatchJobInstance(map);

            }).start();
        }
        return "OK";
    }


    /**
     * 用JdbcTemplate方式执行也没问题，说明不是JdbcTemplate的问题，也不是BATCH_JOB_INSTANCE表本身的问题
     * @return
     */
    @GetMapping("/testInsertMore3")
    public String testInsertMore3(){

        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Object[] parameters = new Object[] { 0, "kunghsu",
                        UUID.randomUUID().toString().replaceAll("-", ""), 0 };

                springBatchDeadLockDemoService.insertBatchJobInstanceByJdbcTemplate(parameters);

            }).start();
        }
        return "OK";
    }


    @GetMapping("/testInsertMore4")
    public String testInsertMore4(){

        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object[] parameters = new Object[] { 0, "kunghsu",
                        UUID.randomUUID().toString().replaceAll("-", ""), 0 };
                springBatchDeadLockDemoService.insertBatchJobInstanceBySameTxManager(parameters);

            }).start();
        }
        return "OK";
    }


    @GetMapping("/testInsertMore5")
    public String testInsertMore5(){

        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object[] parameters = new Object[] { 0, "kunghsu",
                        UUID.randomUUID().toString().replaceAll("-", ""), 0 };
                springBatchDeadLockDemoService.insertBatchJobInstanceByProxy(parameters);

            }).start();
        }
        return "OK";
    }

}
