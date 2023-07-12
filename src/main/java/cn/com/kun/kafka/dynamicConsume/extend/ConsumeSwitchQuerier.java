package cn.com.kun.kafka.dynamicConsume.extend;

/**
 * 消费开关查询接口
 *
 * author:xuyaokun_kzx
 * date:2023/7/12
 * desc:
*/
public interface ConsumeSwitchQuerier {

    /**
     * 查询消费开关
     *
     * @return
     */
    boolean querySwitch();
}
