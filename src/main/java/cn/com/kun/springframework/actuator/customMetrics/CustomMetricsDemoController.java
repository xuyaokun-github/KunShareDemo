package cn.com.kun.springframework.actuator.customMetrics;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat.BatchJobExecTimeStatHelper2;
import cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat.BatchJobExecTimeStatHelper;
import cn.com.kun.springframework.actuator.customMetrics.enums.CustomMetricsEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 */
@RequestMapping("/customMetricsDemo")
@RestController
public class CustomMetricsDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomMetricsDemoController.class);

    @Autowired
    private CustomMetricsService customMetricsService;

    @Autowired
    private MeterRegistry meterRegistry;

    private Map<String, Number> guageMap = new HashMap<>();

    @Autowired
    BatchJobExecTimeStatHelper batchJobExecTimeStatHelper;

    @Autowired
    BatchJobExecTimeStatHelper2 batchJobExecTimeStatHelper2;

    @PostConstruct
    public void init(){
        //注册gauge指标
        Number number = meterRegistry.gauge("My_gauge", new AtomicLong(0));
        guageMap.put("My_gauge", number);

//        Gauge.builder("My_gauge2", gaugeTaskRunHelper, gaugeTaskRunHelper -> gaugeTaskRunHelper.value())
//                .register(meterRegistry);

    }

    @GetMapping("/testGatherCounter")
    public ResultVo testGatherCounter() throws Exception {

        Counter.builder("myCounter")
                .tag("successFlag", ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N")
                .tag("type", "order")
                .tag("oper", "xyk")
                .register(meterRegistry)
                .increment();

        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/testGatherGauge")
    public ResultVo testGatherGauge() throws Exception {

        //测试gauge
//        ((AtomicLong)guageMap.get("My_gauge")).set(ThreadLocalRandom.current().nextLong(1000));

        //启动三个线程，分别执行job
        new Thread(()->{
            String jobName2 = "job2";
            Gauge.builder("My_gauge3", batchJobExecTimeStatHelper, batchJobExecTimeStatHelper -> batchJobExecTimeStatHelper.value(jobName2))
                    .tag("jobName", jobName2)
                    .register(meterRegistry);
            while (true){
                //模拟执行
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(2000, 10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                batchJobExecTimeStatHelper.recordExecTime(jobName2, System.currentTimeMillis() - start);
            }
        }).start();

        new Thread(()->{

            //任务运行前，先注册，任务运行后，记录时间
            String jobName3 = "job3";
            Gauge.builder("My_gauge3", batchJobExecTimeStatHelper, batchJobExecTimeStatHelper -> batchJobExecTimeStatHelper.value(jobName3))
                    .tag("jobName", jobName3)
                    .register(meterRegistry);

            for (int i = 0; i < 10; i++) {
                //模拟执行
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(20000, 30000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                batchJobExecTimeStatHelper.recordExecTime(jobName3, System.currentTimeMillis() - start);
            }
        }).start();

        new Thread(()->{
            String jobName = "job1";
            Gauge.builder("My_gauge3", batchJobExecTimeStatHelper, batchJobExecTimeStatHelper -> batchJobExecTimeStatHelper.value(jobName))
                    .tag("jobName", jobName)
                    .register(meterRegistry);
            while (true){
                //模拟执行
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(2000, 10000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                batchJobExecTimeStatHelper.recordExecTime(jobName, System.currentTimeMillis() - start);
            }
        }).start();


        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/testGatherGaugeRun10Times")
    public ResultVo testGatherGaugeRun10Times() throws Exception {

        //任务运行前，先注册，任务运行后，记录时间(这个注册过程，不需要每次都注册，注册过无需再注册)
        String jobName3 = "job3";
        batchJobExecTimeStatHelper.registerGaugeMetris(jobName3);

        for (int i = 0; i < 10; i++) {
            //模拟执行
            //执行频率比采集频率慢
//            Thread.sleep(15000);
            Thread.sleep(5200);
            //执行频率比采集频率快
//            Thread.sleep(800);
            long start = System.currentTimeMillis();
            try {
                //执行频率比
                Thread.sleep(ThreadLocalRandom.current().nextLong(100, 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long cost = System.currentTimeMillis() - start;
            LOGGER.info("记录运行时间：{} 运行次数：{}", cost, i);
            batchJobExecTimeStatHelper.recordExecTime(jobName3, cost);
        }
        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/testGatherTimer")
    public ResultVo testGatherTimer() throws Exception {


        String[] tags = new String[]{"name", "xyk"};
        Timer timer = Timer.builder("My_timer")
                .tags(tags)
                .description("desc xxxxxxx")
                .register(meterRegistry);
        timer.record(()->{
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        });

        Timer.Sample sample = Timer.start(meterRegistry);
        Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
        //创建定义计数器，并设置指标的Tags信息（名称可以自定义）
        Timer timer2 = Timer.builder("My_timer2")
                .tag("taskName", "Job1")
                .description("desc xxxxxxx")
                .register(meterRegistry);
        sample.stop(timer2);

        Timer.Sample sample2 = Timer.start(meterRegistry);
        Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
        //创建定义计数器，并设置指标的Tags信息（名称可以自定义）
        Timer timer3 = Timer.builder("My_timer2")
                .tag("taskName", "Job2")
                .description("desc xxxxxxx")
                .register(meterRegistry);
        sample2.stop(timer3);

        return ResultVo.valueOfSuccess("OK");

    }

    @GetMapping("/testGatherTimer2")
    public ResultVo testGatherTimer2() throws Exception {

        //启动三个线程，分别执行job
        new Thread(()->{
            String jobName2 = "job2";
            while (true){
                //隔久一点再执行任务
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                batchJobExecTimeStatHelper2.execTask("My_timer3", jobName2, ()->{
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(500, 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                });
            }
        }).start();

        new Thread(()->{

            //任务运行前，先注册，任务运行后，记录时间
            String jobName3 = "job3";
            for (int i = 0; i < 10; i++) {
                batchJobExecTimeStatHelper2.execTask("My_timer3", jobName3, ()->{
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                });
            }
        }).start();

        new Thread(()->{
            String jobName = "job1";
            while (true){
                batchJobExecTimeStatHelper2.execTask("My_timer3", jobName, ()->{
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(500, 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "";
                });
            }
        }).start();

        return ResultVo.valueOfSuccess("OK");

    }


    @GetMapping("/testGatherTimer3")
    public ResultVo testGatherTimer3() throws Exception {

        String jobName3 = "job3";
        for (int i = 0; i < 10; i++) {
            batchJobExecTimeStatHelper2.execTask("My_timer3", jobName3, ()->{
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(20000, 30000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
            });
        }
        return ResultVo.valueOfSuccess("OK");

    }
    @GetMapping("/testGatherTimer4")
    public ResultVo testGatherTimer4() throws Exception {

        String jobName3 = "job3";
        for (int i = 0; i < 10; i++) {
            Thread.sleep(8000);
            batchJobExecTimeStatHelper2.execTask("My_timer3", jobName3, ()->{
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(200, 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
            });
        }
        return ResultVo.valueOfSuccess("OK");

    }


    @GetMapping("/testByOptimization")
    public ResultVo testByOptimization() throws Exception {

        //优化后
        customMetricsService.gather(CustomMetricsEnum.MYCUSTOMMETRICS_ONE.counterName,
                ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                "order",
                "xyk");
        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/testByOptimization2OneTimes")
    public ResultVo testByOptimization2OneTimes() throws Exception {

        //优化后
        customMetricsService.gather(CustomMetricsEnum.MYTWO.counterName,
                ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                "order",
                "kunghsu",
                "" + UUID.randomUUID().toString());

        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/testByOptimization2")
    public ResultVo testByOptimization2() throws Exception {

        for (int i = 0; i < 10; i++) {
            ThreadUtils.runAsyncByRunnable(()->{
                while (true){
                    //优化后
                    customMetricsService.gather(CustomMetricsEnum.MYTWO.counterName,
                            ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                            "order",
                            "kunghsu",
                            "" + UUID.randomUUID().toString());
                }
            });
        }


        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/testByOptimization2BySleepOneSeconds")
    public ResultVo testByOptimization2BySleepOneSeconds() throws Exception {

        for (int i = 0; i < 10; i++) {
            ThreadUtils.runAsyncByRunnable(()->{
                while (true){
                    //优化后
                    customMetricsService.gather(CustomMetricsEnum.MYTWO.counterName,
                            ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                            "order",
                            "kunghsu",
                            "" + UUID.randomUUID().toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/testActivityReject")
    public ResultVo testActivityReject() throws Exception {

        int count = ThreadLocalRandom.current().nextInt(10);
        int count2 = ThreadLocalRandom.current().nextInt(10);
        for (int i = 0; i < count; i++) {
            customMetricsService.gather(CustomMetricsEnum.ACTIVITY_REJECT_STAT.counterName,
                    "000001");
        }
        for (int i = 0; i < count2; i++) {
            customMetricsService.gather(CustomMetricsEnum.ACTIVITY_REJECT_STAT.counterName,
                    "000003");
        }
        return ResultVo.valueOfSuccess("OK");
    }


}
