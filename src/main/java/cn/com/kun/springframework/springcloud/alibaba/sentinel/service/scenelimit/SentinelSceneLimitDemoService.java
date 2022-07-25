package cn.com.kun.springframework.springcloud.alibaba.sentinel.service.scenelimit;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.FlowMonitorCallback;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.SentinelFlowMonitor;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.SentinelRuleLoader;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.vo.FlowMonitorRes;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.*;

/**
 * 按场景限流demo
 * 多规则限流--分场景限流
 *
 * <p>
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
 */
@Service
public class SentinelSceneLimitDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelSceneLimitDemoService.class);

    private AtomicLong sleepMillis = new AtomicLong(100L);

    @Autowired
    private SentinelFlowMonitor sentinelFlowMonitor;

    private FlowMonitorRes flowMonitorRes;

    public FlowMonitorRes getFlowMonitorRes() {
        return flowMonitorRes;
    }

    public void setFlowMonitorRes(FlowMonitorRes flowMonitorRes) {
        this.flowMonitorRes = flowMonitorRes;
    }

    @Autowired
    SentinelRuleLoader sentinelRuleLoader;

    @PostConstruct
    public void init(){


        sentinelRuleLoader.addFlowRules(buildTestRules());

        //测试方法
        sentinelRuleLoader.addDegradeRules(buildDegradeRuleList());


        /*
            注册一个回调
            注册动作由使用方自行决定
         */
        sentinelFlowMonitor.registFlowMonitorCallback(CONTEXT_MSG_PUSH, new FlowMonitorCallback() {
            @Override
            public void monitorCallback(FlowMonitorRes flowMonitorRes) {
//                LOGGER.info("触发了业务层注册的回调逻辑：{}", flowMonitorRes);
                setFlowMonitorRes(flowMonitorRes);
            }
        });

//        for (int i = 0; i < 10; i++) {
//            new Thread(()->{
//                while (true){
//                    try {
//                        //
//                        method(null, "DX");
//                        method(null, "WX");
//                        Thread.sleep(sleepMillis.get());
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
    }


    private List<DegradeRule> buildDegradeRuleList() {

        List<DegradeRule> degradeRuleList = new ArrayList<>();

        //测试例子
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

        DegradeRule degradeRule2 = new DegradeRule(RESOURCE_NAME_4)
                .setGrade(RuleConstant.DEGRADE_GRADE_RT)
                // Max allowed response time
                //假如grade策略指定了为DEGRADE_GRADE_RT，count表示超时时长
                .setCount(1000)
                // Retry timeout (in second)
                .setTimeWindow(60);
        degradeRuleList.add(degradeRule2);
        return degradeRuleList;
    }


    /**
     * 测试方法
     * 验证规则的使用
     */
    private List<FlowRule> buildTestRules() {

        List<FlowRule> rules = new ArrayList<>();
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

        rules.add(rule);
        rules.add(rule2);

        //通过加载配置类，逐一添加
//        rules.add(buildFlowRule(RESOURCE_SCENE_WX, 10, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
//        rules.add(buildFlowRule(RESOURCE_SCENE_DX, 5, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        rules.add(buildFlowRule(RESOURCE_NAME_3, 200, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        rules.add(buildFlowRule(RESOURCE_NAME_TESTLIMITANDDEGRADE2, 200, RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        return rules;
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




    /**
     * 根据不同的入参决定采用哪个限流配置
     * 限流后不会重试
     * @param paramMap
     * @param sendChannel
     * @return
     */
    public ResultVo method(Map<String, String> paramMap, String sendChannel) {

        ContextUtil.enter(CONTEXT_MSG_PUSH);

        //根据业务标识，决定采用哪个限流配置
        String resourceName = "";
        if ("DX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_DX;
        } else if ("WX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_WX;
        }

        try (Entry entry = SphU.entry(resourceName)) {
            // 被保护的逻辑
            return doWork(paramMap, sendChannel);
        } catch (BlockException ex) {
            //处理被流控的逻辑
            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//            LOGGER.error("触发限流--" + sendChannel, ex);
            //假如出现了限流，由当前线程一直处理，或者是丢弃、或者是放到其他队列，由异步线程去逐个处理
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     * 限流后会进行重试
     * @param paramMap
     * @param sendChannel
     * @return
     */
    public ResultVo method2(Map<String, String> paramMap, String sendChannel) {

        //ContextUtil.enter标记调用链路
        ContextUtil.enter(CONTEXT_MSG_PUSH);

        ResultVo res = null;
        //根据业务标识，决定采用哪个限流配置
        String resourceName = "";
        if ("DX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_DX;
        } else if ("WX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_WX;
        }

        while (true){
            try (Entry entry = SphU.entry(resourceName)) {
                // 被保护的逻辑
                res = doWork(paramMap, sendChannel);
                break;
            } catch (Exception ex) {
                if (ex instanceof BlockException){
                    //BlockException
                    //处理被流控的逻辑
                    //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//                    LOGGER.error("触发限流--" + sendChannel, ex);
                    //假如出现了限流，由当前线程一直处理，或者是丢弃、或者是放到其他队列，由异步线程去逐个处理
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    res = ResultVo.valueOfError("出现非限流异常");
                    break;
                }
            }
        }
        return res;
    }


    private ResultVo doWork(Map<String, String> paramMap, String sendChannel){
//        LOGGER.info("SceneLimitDemoService在调用第三方接口，场景：{}", sendChannel);
        try {
            //加个超时时间，验证 匀速通过场景的超时时间
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        LOGGER.info("SceneLimitDemoService结束调用第三方接口，场景：{}", sendChannel);
        return ResultVo.valueOfSuccess();
    }

}
