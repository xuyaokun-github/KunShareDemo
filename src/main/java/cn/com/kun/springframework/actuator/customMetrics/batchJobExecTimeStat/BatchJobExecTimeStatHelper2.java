package cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 配合业务监控使用--基于Timer统计批处理运行时间
 *
 * author:xuyaokun_kzx
 * date:2022/4/11
 * desc:
*/
@Component
public class BatchJobExecTimeStatHelper2 {

    @Autowired
    private MeterRegistry meterRegistry;

    public <T> T execTask(String timerName, String jobName, Supplier<T> f) {

        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return f.get();
        } finally {
            //创建定义计数器，并设置指标的Tags信息（名称可以自定义）
            Timer timer2 = Timer.builder(timerName)
                    .tag("taskName", jobName)
                    .description("desc xxxxxxx")
                    .register(meterRegistry);
            sample.stop(timer2);
        }

    }

}
