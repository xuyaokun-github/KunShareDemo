package cn.com.kun.springframework.actuator.customMetrics;

import cn.com.kun.springframework.actuator.customMetrics.enums.CustomMetricsEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 这个类可以提取成一个公共组件，注入到各个服务层中使用
 *
 * author:xuyaokun_kzx
 * date:2021/11/24
 * desc:
*/
@Service
public class CustomMetricsServiceImpl implements CustomMetricsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomMetricsServiceImpl.class);

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * counterName -> tagList
     * 例如：
     */
    private Map<String, String[]> tagListMap = new HashMap<>();

    /**
     * 是否超出最大采集项阈值
     */
    private boolean moreThanMaxCountThreshold;

    @PostConstruct
    public void init(){

        //遍历枚举类，放入initialCacheConfigurations
        for(CustomMetricsEnum customMetricsEnum : CustomMetricsEnum.values()) {
            tagListMap.put(customMetricsEnum.counterName, customMetricsEnum.tagList.split(","));
        }

        runMonitorThread();
    }

    private void runMonitorThread() {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "System Clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            monitor();}, 1, 1, TimeUnit.SECONDS);

        //起一个异步线程监控，而且是永久监控
//        new Thread(()->{
//            while (true){
//                //可以每隔一秒监控一次，减少CPU占用
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                monitor();
//            }
//        }).start();

    }

    private void monitor(){
        //设置一个阈值，假如超过这个阈值，则不要再继续采集了
        //阈值的大小和排列组合的个数有关，假如tag很多，则排列组合个数就会有很多
        List<Meter> meterList = meterRegistry.getMeters();
        if (meterList.size() > 200000){
            moreThanMaxCountThreshold = true;
        }
    }

    /**
     * 所有需要监控的地方只需要调这个方法即可,好处就是不需要额外写监控方法
     * 这样优化后，开发需要开发一个新的业务监控只需要两步：
     * 1.在枚举类里加一个项
     * 2.在具体业务代码里调用cn.com.kun.springframework.actuator.CustomMetricsServiceImpl#gather(java.lang.String, java.lang.String...)方法
     *
     * @param counterName
     * @param values
     */
    @Override
    public void gather(String counterName, String... values) {

        //假如values一直变，可能会有内存泄漏的风险，导致内置map元素一直增多，详情见我的源码分析章节
        //简单的方法就是人为控制保证调该方法时传入的值不会太多变化，但是这个暴露出去，不懂的开发人员可能会误用、
        //这里是否可以对values进行一次过滤？
        //针对每个value,可能会出现什么值，进行一次过滤，假如出现了预估之外的值，则不做采集了
        /*
            为什么有必要做过滤限制？
            因为采集漏问题不大，因为它本身就不是一个主流程的东西
            但是一旦发生了内存泄漏，系统整体可能就不可用了，因小失大。

            或者用另一个方法，判断下内存的map是否过大，假如排列组合的个数太多，则停止采集，避免问题进一步发生。
            相当于做了一层防线，即使开发人员误用了，也不会导致map无限变大
            判断是否超限的逻辑应该交由异步线程完成，假如在本方法中做判断，会影响原业务的TPS，采集这个过程本来就应该是轻量的
         */

        if (moreThanMaxCountThreshold){
            //则给出告警，提示不再采集了，内存扛不住了
            //这个日志不应该一直输出，应该有间隔地输出，假如一直输出，日志量会很多
            LOGGER.info("业务监控采集排列组合个数过多，已超20万，已关闭采集入口。请检查统计条件，优化采集逻辑");
            return;
        }

        //监控值数组
        Counter.Builder builder = Counter.builder(counterName);

        //通过监控器名，拿到tag列映射关系
        String[] tags = tagListMap.get(counterName);
        if (tags == null || tags.length != values.length){
            return;
        }
        for (int i = 0; i < values.length; i++) {
            builder.tag(tags[i], values[i]);
        }
        builder.register(meterRegistry).increment();
    }

}
