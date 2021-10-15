package cn.com.kun.springframework.springcloud.alibaba.sentinel.properties;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

/**
 * 限流规则的扩展
 * 继承自com.alibaba.csp.sentinel.slots.block.flow.FlowRule
 *
 * author:xuyaokun_kzx
 * date:2021/10/14
 * desc:
*/
public class CustomFlowRule extends FlowRule {

    private Long yellowLineThreshold;

    public Long getYellowLineThreshold() {
        return yellowLineThreshold;
    }

    public void setYellowLineThreshold(Long yellowLineThreshold) {
        this.yellowLineThreshold = yellowLineThreshold;
    }
}
