package cn.com.kun.kafka.dynamicConsume.extend;

import java.util.List;

/**
 * 消费开关查询接口
 *
 * author:xuyaokun_kzx
 * date:2023/7/12
 * desc:
*/
public interface ConsumeSwitchQuerier {

    /**
     * 查询消费开关(支持根据主题查询不同的开关)
     *
     * @return
     */
    boolean querySwitch(List<String> topicList);


}
