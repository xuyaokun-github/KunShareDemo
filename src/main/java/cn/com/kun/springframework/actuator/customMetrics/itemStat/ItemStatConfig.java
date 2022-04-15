package cn.com.kun.springframework.actuator.customMetrics.itemStat;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemStatConfig {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private ActivityDemoService activityDemoService;

    /**
     * 假如有多个业务维度需要统计，则自定义即可
     * 假如需要扩展tags,可以改下代码
     * @return
     */
    @Bean
    public ItemStatHelper itemStatHelper(){

        ItemStatHelper itemStatHelper = new ItemStatHelper("My_Item_ACTIVITY", meterRegistry);
        //创建一个检测函数
        itemStatHelper.setCheckItemFinishFunction(new CheckItemFinishFunction() {
            @Override
            public boolean check(Object value) {
                return activityDemoService.checkActivityFinished(value);
            }
        });
        itemStatHelper.startRemoveThread();
        return itemStatHelper;
    }


}
