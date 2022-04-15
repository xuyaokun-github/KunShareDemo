package cn.com.kun.springframework.actuator.customMetrics.countdemo;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat.BatchJobExecTimeStatHelper2;
import cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat.BatchJobExecTimeStatHelper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

/**
 */
@RequestMapping("/customMetricsDemo-count")
@RestController
public class CustomMetricsCountDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomMetricsCountDemoController.class);

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    BatchJobExecTimeStatHelper batchJobExecTimeStatHelper;

    @Autowired
    BatchJobExecTimeStatHelper2 batchJobExecTimeStatHelper2;

    private AtomicReference reference = new AtomicReference();

    @PostConstruct
    public void init(){

    }

    @GetMapping("/testCounter")
    public ResultVo testCounter() throws Exception {

        Counter counter = Counter.builder("My_Counter")
                .tag("activityId", "001")
                .register(meterRegistry);

        reference.set(counter);

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                counter.increment();
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextLong(5000, 9000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/testRemoveCounter")
    public ResultVo testRemoveCounter() throws Exception {

        meterRegistry.remove((Meter) reference.get());

        return ResultVo.valueOfSuccess("OK");
    }


}
