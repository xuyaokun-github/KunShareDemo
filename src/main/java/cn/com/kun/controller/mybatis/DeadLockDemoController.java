package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import cn.com.kun.service.mybatis.DeadLockDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
