package cn.com.kun.kafka.autoSwitch.core;

import cn.com.kun.kafka.autoSwitch.vo.ControlCenterQryResult;
import org.springframework.web.client.RestTemplate;

/**
 * 主题控制中心访问器
 *
 * author:xuyaokun_kzx
 * date:2023/5/4
 * desc:
*/
public class TopicControlCenterVisitor {

    //默认实现，使用RestTemplate
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 控制中心的访问URL
     */
    private String url = null;

    public TopicControlCenterVisitor(String url) {
        this.url = url;
    }

    public ControlCenterQryResult qryClusters(){

        ControlCenterQryResult controlCenterQryResult = restTemplate.getForObject(this.url, ControlCenterQryResult.class);
        return controlCenterQryResult;
    }
}
