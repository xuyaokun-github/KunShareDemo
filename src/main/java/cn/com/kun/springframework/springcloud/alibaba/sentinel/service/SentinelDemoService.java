package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import cn.com.kun.common.utils.ThreadUtils;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SentinelDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelDemoService.class);

    private String RESOURCE_NAME = "cn.com.kun.springframework.springcloud.alibaba.sentinel.service.SentinelDemoService.testSimpleLimit";

    private String RESOURCE_NAME2 = "testSimpleLimit2";

    @PostConstruct
    public void init(){
        initFlowRules();
    }

    private void initFlowRules(){
        FlowRule rule = new FlowRule();
        rule.setResource(RESOURCE_NAME);
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //每个线程的qps最多是2
        rule.setCount(2);

        FlowRule rule2 = new FlowRule();
        rule2.setResource(RESOURCE_NAME2);
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule2.setCount(2);

        List<FlowRule> rules = new ArrayList<>();
        rules.add(rule);
        rules.add(rule2);

        FlowRuleManager.loadRules(rules);
    }

    /**
     * 简单限流
     */
    public String testSimpleLimit(){

        try (Entry entry = SphU.entry(RESOURCE_NAME)) {
            // 被保护的逻辑
            LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
            LOGGER.error("触发限流", ex);
        }
        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

    /**
     * 简单限流(注解方式，直接能用，无法做额外的配置)
     */
    @SentinelResource(value = "testSimpleLimit2", blockHandler = "exceptionHandler")
    public String testSimpleLimit2(){

        // 被保护的逻辑
        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());
        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }


    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(BlockException ex) {
        LOGGER.error("触发限流", ex);
        return "请求过于频繁";
    }

    /**
     * 多规则限流--分场景限流
     */

}
