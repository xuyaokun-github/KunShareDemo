package cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 配合业务监控使用--基于Guage统计批处理运行时间
 * 无论采集频率和任务执行频率哪个更快，都可以采集，只是采集的没有Timer准确，但是Timer展示出来的图表无法反馈具体执行了多少次
 *
 * 假设采集频率 比 运行频率快，这种情况下每一次执行耗时都会被统计到，自然也会展示到图表上
 * 假设运行频率 比 采集频率快，可能会有些运行次数被覆盖！！ 但假如这种覆盖是能接受的，我觉得也没问题。
 * 但实践中，批处理的运行频率不会像心跳检测一样，一秒一次，批处理的频率通常都是分钟级别。
 * 所以用Guage这个方案完全没问题
 *
 * author:xuyaokun_kzx
 * date:2022/4/11
 * desc:
*/
@Component
public class BatchJobExecTimeStatHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(BatchJobExecTimeStatHelper.class);

    /**
     * 假设普罗米修斯服务端每5秒采集一次，定时任务在5秒内运行了3次，但最终只会被统计一次执行耗时
     * 所以要统计得很频繁的定时任务执行耗时，本组件不支持
     */
    private Map<String, AtomicLong> execTimeMap = new ConcurrentHashMap<>();

    private Set<String> registerSet = new HashSet<>();

    private final String GAUGE_METRICS_NAME = "batch_job_exec_cost";

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * 在任务结束后记录执行时间
     * @param jobName
     * @param execTimeMs
     */
    public void recordExecTime(String jobName, Long execTimeMs){

        AtomicLong atomicLong = execTimeMap.get(jobName);
        if (atomicLong == null){
            atomicLong = new AtomicLong(0);
            execTimeMap.put(jobName, atomicLong);
        }
        synchronized (atomicLong){
            atomicLong.set(execTimeMs);
        }
    }

    /**
     *
     * @param jobName
     * @return
     */
    public double value(String jobName) {

        AtomicLong atomicLong = execTimeMap.get(jobName);
        if (atomicLong != null){
            synchronized (atomicLong){
                long execTime = atomicLong.get();
                if (execTime > 0){
                    //
                    atomicLong.set(0);
                    LOGGER.info("采集到的耗时数据：{}", execTime);
                    return execTime;
                }
            }
        }else {
            execTimeMap.put(jobName, new AtomicLong(0));
        }
        return 0;
    }

    public void registerGaugeMetris(String jobName) {

        if (!registerSet.contains(jobName)){
            Gauge.builder(GAUGE_METRICS_NAME, this, batchJobExecTimeStatHelper -> batchJobExecTimeStatHelper.value(jobName))
                    .tag("jobName", jobName)
                    .register(meterRegistry);
            registerSet.add(jobName);
        }
    }
}
