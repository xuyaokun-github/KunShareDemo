package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME;
import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME2;

@Service
public class FlowRuleLoadService {

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


}
