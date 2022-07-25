package cn.com.kun.component.sentinel.sentinelFlowMonitor.properties;

import cn.com.kun.component.sentinel.sentinelFlowMonitor.vo.CustomFlowRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * 限流的配置
 *
 * author:xuyaokun_kzx
 * date:2021/6/30
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="sentinel-rule")
public class SentinelRuleProperties implements Serializable {

    /**
     * 限流规则
     */
    private Map<String, CustomFlowRule> flowRule;

    /**
     * 降级规则
     */
    private Map<String, DegradeRule> degradeRule;


    public Map<String, CustomFlowRule> getFlowRule() {
        return flowRule;
    }

    public void setFlowRule(Map<String, CustomFlowRule> flowRule) {
        this.flowRule = flowRule;
    }

    public Map<String, DegradeRule> getDegradeRule() {
        return degradeRule;
    }

    public void setDegradeRule(Map<String, DegradeRule> degradeRule) {
        this.degradeRule = degradeRule;
    }
}
