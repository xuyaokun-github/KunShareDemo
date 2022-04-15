package cn.com.kun.springframework.actuator.customMetrics.itemStat;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 针对不需要长期绘制的图表的统计场景，例如统计某个发券活动的拒绝次数
 * 每增加一个维度，就可以增加一个该bean
 * 例如：
 * 1.统计某一个活动在开展期间的所有用户拒绝次数
 * 2.统计某一个任务在运行期间的某种行为的次数
 * 这类需求都是针对某个维度，统计某一次单项所展示出来的轨迹
 * 一个活动ID就是一个单项，所以该组件起名为ItemStatHelper
 *
 * author:xuyaokun_kzx
 * date:2022/4/15
 * desc:
*/
public class ItemStatHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(ItemStatHelper.class);

    private String metricsName;

    private MeterRegistry meterRegistry;

    /**
     * 每一个ItemId就对应一个指标
     */
    private Map<String, Counter> counterMap = new ConcurrentHashMap<>();

    /**
     * 检查是否ItemId对应的项是否已经完成，无需继续继续监控
     */
    private CheckItemFinishFunction checkItemFinishFunction;

    public ItemStatHelper(String metricsName, MeterRegistry meterRegistry) {
        if (StringUtils.isEmpty(metricsName)){
            throw new RuntimeException("指标名不能为空");
        }
        this.metricsName = metricsName;
        this.meterRegistry = meterRegistry;
    }

    public CheckItemFinishFunction getCheckItemFinishFunction() {
        return checkItemFinishFunction;
    }

    public void setCheckItemFinishFunction(CheckItemFinishFunction checkItemFinishFunction) {
        this.checkItemFinishFunction = checkItemFinishFunction;
    }

    /**
     * 增加
     */
    public void increment(String itemId, double amount){

        //先判断是否有注册，假如有注册过，无需继续注册了
        Counter counter = counterMap.get(itemId);
        if (counter == null){
            //没有注册过，则需要注册(因为并发重复注册也没关系，这里不上锁了)
            counter = Counter.builder(metricsName)
                    .tag("itemId", itemId)
                    .register(meterRegistry);
            counterMap.put(itemId, counter);
        }
        counter.increment(amount);

    }

    /**
     * 是否开启，由用户自行决定
     */
    public void startRemoveThread(){

        //开启指标移除线程
        new Thread(()->{
            //这个线程该不该推出？假如将整个功能关闭后，该线程也应该退出，可以加一个中断标记
            while (true){
                removeFinishedMetrics();
            }
        }).start();
    }

    private void removeFinishedMetrics() {

        try {
            //这个间隔可以设置久一点，假如设值太小，加重服务器负担，假如设置太大，失效的图线会保持很久，所以设置一个折中的值就可以
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //filter 为true的则保留 说明是已完成
        Iterator iterator = counterMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            if (isFinished(key)){
                LOGGER.info("清除已完成指标：{}", key);
                Meter meter = counterMap.get(key);
                meterRegistry.remove(meter);
                iterator.remove();
            }
        }
    }

    private boolean isFinished(String key) {
        return checkItemFinishFunction.check(key);
    }

    public String getMetricsName() {
        return metricsName;
    }

    public void setMetricsName(String metricsName) {
        this.metricsName = metricsName;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }

    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

}
