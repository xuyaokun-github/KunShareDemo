package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.FlowMonitorProcessor;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.*;

@Service
public class FlowRuleLoadService {

    @Autowired
    FlowMonitorProcessor flowMonitorProcessor;

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
        rules.add(buildFlowRule(RESOURCE_SCENE_DX, 5, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        rules.add(buildFlowRule(RESOURCE_SCENE_WX, 10, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        rules.add(buildFlowRule(RESOURCE_NAME_3, 200, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        rules.add(buildFlowRule(RESOURCE_NAME_TESTLIMITANDDEGRADE2, 200, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));

        FlowRuleManager.loadRules(rules);

        flowMonitorProcessor.registContextName("sentinel_default_context");
        flowMonitorProcessor.registContextName("MSG_PUSH");

        //设置监控的绿黄分隔线
        //当QPS超过20就触发黄色预警
        flowMonitorProcessor.registGreedYellowLineThreshold(RESOURCE_NAME, 20L);


        //------------------降级规则---------------------------
        List<DegradeRule> degradeRuleList = new ArrayList<>();
        //给资源RESOURCE_NAME，指定一个降级规则
        DegradeRule degradeRule = new DegradeRule(RESOURCE_NAME_3)
                .setGrade(RuleConstant.DEGRADE_GRADE_RT)
                // Max allowed response time
                //假如grade策略指定了为DEGRADE_GRADE_RT，count表示超时时长
                .setCount(1000)
                // Retry timeout (in second)
                .setTimeWindow(60);
                // Circuit breaker opens when slow request ratio > 60% (下面三个都是后面版本才有的)
//                .setSlowRatioThreshold(0.6)
//                .setMinRequestAmount(100)
//                .setStatIntervalMs(20000);

        degradeRuleList.add(degradeRule);
        degradeRuleList.add(buildDegradeRule(RESOURCE_NAME_TESTLIMITANDDEGRADE2, RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT, 1, 120));
        DegradeRuleManager.loadRules(degradeRuleList);
    }

    private DegradeRule buildDegradeRule(String resource, int grade, int count, int timeWindow) {
        DegradeRule degradeRule = new DegradeRule(resource)
                .setGrade(grade)
                // Max allowed response time
                //假如grade策略指定了为DEGRADE_GRADE_RT，count表示超时时长
                .setCount(count)
                // Retry timeout (in second)
                .setTimeWindow(timeWindow);
        return degradeRule;
    }

    private FlowRule buildFlowRule(String resource, int count, int controlBehavior) {
        FlowRule rule2 = new FlowRule();
        rule2.setResource(resource);
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        /*
            流控策略
            CONTROL_BEHAVIOR_RATE_LIMITER：匀速通过，请求不会被拒绝。当时被限流的请求会被缓慢地被执行
         */
        rule2.setControlBehavior(controlBehavior);
        // 流控效果, 采用warm up冷启动方式
//        rule2.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        // 在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮。
        // warmUpPeriodSec 代表期待系统进入稳定状态的时间（即预热时长）。
        // 这里预热时间为1min, 便于在dashboard控制台实时监控查看QPS的pass和block变化曲线
//        rule2.setWarmUpPeriodSec(60); // 默认值为10s
        /*
            使用CONTROL_BEHAVIOR_RATE_LIMITER时，可以指定在排队执行时的超时时间
            这里设置多少是有讲究的，设置太小，可能当前通过的还没执行完，下一个不能通过，所以队列里的会超时，然后抛出限流异常
            假如超时时间设置太大，能保证全都执行完，但放到队列里的内容可能会很多
         */
        rule2.setMaxQueueingTimeMs(5000000);//默认是500ms
        rule2.setCount(count);
        return rule2;
    }




}
