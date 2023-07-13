package cn.com.kun.kafka.dynamicConsume.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/29
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kafka.dynamic-consume")
public class KafkaDynamicConsumeProperties implements Serializable {

    /**
     * 起使时间（小时）
     */
    private int startTimeHour;

    /**
     * 终止时间（小时）
     */
    private int endTimeHour;


    public int getStartTimeHour() {
        return startTimeHour;
    }

    public void setStartTimeHour(int startTimeHour) {
        this.startTimeHour = startTimeHour;
    }

    public int getEndTimeHour() {
        return endTimeHour;
    }

    public void setEndTimeHour(int endTimeHour) {
        this.endTimeHour = endTimeHour;
    }
}
