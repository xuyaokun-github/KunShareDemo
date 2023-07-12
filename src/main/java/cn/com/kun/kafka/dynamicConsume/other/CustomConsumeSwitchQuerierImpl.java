package cn.com.kun.kafka.dynamicConsume.other;

import cn.com.kun.kafka.dynamicConsume.extend.ConsumeSwitchQuerier;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 自定义的开关查询器
 *
 * author:xuyaokun_kzx
 * date:2023/7/12
 * desc:
*/
@Component
public class CustomConsumeSwitchQuerierImpl implements ConsumeSwitchQuerier {

    private boolean consumeSwitch = true;

    private int startTime = 6;

    private int endTime = 23;


    @Override
    public boolean querySwitch() {

        /**
         * 如何向组件返回 消费开关 这个是业务逻辑部分
         * 通常最常见的是 凌晨停止，白天开启
         */

        //第一种逻辑： 分钟数在 双数时才做消费
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int minute = calendar.get(Calendar.MINUTE);
        if (minute % 2 == 0){
            return true;
        }else {
            return false;
        }

//        return consumeSwitch;
    }

    public void setConsumeSwitch(boolean consumeSwitch) {
        this.consumeSwitch = consumeSwitch;
    }
}
