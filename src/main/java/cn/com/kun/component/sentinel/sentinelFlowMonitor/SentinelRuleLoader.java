package cn.com.kun.component.sentinel.sentinelFlowMonitor;

import cn.com.kun.component.sentinel.sentinelFlowMonitor.properties.SentinelRuleProperties;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.vo.CustomFlowRule;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class SentinelRuleLoader {

    @Autowired
    private SentinelFlowMonitor sentinelFlowMonitor;

    @Autowired
    private SentinelRuleProperties sentinelRuleProperties;

    @PostConstruct
    public void init(){
        initFlowRules();
        initFlowMonitorInfo();
    }

    /**
     * 初始化监控相关的信息：
     * 需要监控的context（调用链路）
     * 各个资源的黄色预警线
     */
    private void initFlowMonitorInfo() {

        Map<String, CustomFlowRule> flowRuleMap = sentinelRuleProperties.getFlowRule();
        Iterator iterator = flowRuleMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            CustomFlowRule flowRule = (CustomFlowRule) entry.getValue();
            if (flowRule.getYellowLineThreshold() != null){
                sentinelFlowMonitor.registYellowLineThreshold(key, flowRule.getYellowLineThreshold());
            }
        }

    }

    private void initFlowRules(){

        //------------------限流规则---------------------------
        List<FlowRule> rules = new ArrayList<>();

        //添加通过配置文件定义的规则
        addFlowRuleFromConfig(rules);
        //加载
        FlowRuleManager.loadRules(rules);

        //------------------熔断降级规则---------------------------
        List<DegradeRule> degradeRuleList = new ArrayList<>();
        addDegradeRuleFromConfig(degradeRuleList);
        //加载
        DegradeRuleManager.loadRules(degradeRuleList);

    }

    private void addDegradeRuleFromConfig(List<DegradeRule> degradeRuleList) {

        Map<String, DegradeRule> degradeRuleMap = sentinelRuleProperties.getDegradeRule();
        Iterator iterator = degradeRuleMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            DegradeRule degradeRule1 = (DegradeRule) entry.getValue();
            degradeRuleList.add(buildDegradeRule(key, degradeRule1.getGrade(), degradeRule1.getCount(), degradeRule1.getTimeWindow()));
        }
    }


    public void addFlowRules(List<FlowRule> rules) {

        FlowRuleManager.getRules().addAll(rules);
    }


    public void addDegradeRules(List<DegradeRule> degradeRules) {

        DegradeRuleManager.getRules().addAll(degradeRules);
    }

    private void addFlowRuleFromConfig(List<FlowRule> rules) {

        Map<String, CustomFlowRule> flowRuleMap = sentinelRuleProperties.getFlowRule();
        Iterator iterator = flowRuleMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            FlowRule flowRule = (FlowRule) entry.getValue();
            rules.add(buildFlowRule(key, flowRule.getCount(), RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        }
    }

    private DegradeRule buildDegradeRule(String resource, int grade, double count, int timeWindow) {
        DegradeRule degradeRule = new DegradeRule(resource)
                .setGrade(grade)
                // Max allowed response time
                //假如grade策略指定了为DEGRADE_GRADE_RT，count表示超时时长
                .setCount(count)
                // Retry timeout (in second) 单位是秒
                .setTimeWindow(timeWindow);
        return degradeRule;
    }

    private FlowRule buildFlowRule(String resource, double count, int controlBehavior) {

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
//        rule2.setMaxQueueingTimeMs(5000000);//默认是500ms
        rule2.setCount(count);
        return rule2;
    }


}
