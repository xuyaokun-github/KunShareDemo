package cn.com.kun.springframework.springcloud.alibaba.sentinel.demo1;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * ！
 *
 * author:xuyaokun_kzx
 * date:2021/9/29
 * desc:
*/
public class FLOWGRADEQPSSentinelTest {

    public static void main(String[] args) {
        // 配置规则.
        initFlowRules();

//        method1();
        method2();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不会触发限流
     */
    private static void method1() {
        for (int i = 0; i < 25; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1; j++) {
                    // 1.5.0 版本开始可以直接利用 try-with-resources 特性
                    //支持Entry的自动close，close方法里最终会调用到exit方法
                    try (Entry entry = SphU.entry("HelloWorld2")) {
                        // 被保护的逻辑
                        System.out.println("hello world");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (BlockException ex) {
                        // 处理被流控的逻辑
                        //com.alibaba.csp.sentinel.slots.block.flow.FlowException
                        ex.printStackTrace();
                        System.out.println("blocked!");
                    }
                }
            }).start();
        }
    }

    /**
     * 会触发限流
     */
    private static void method2() {
        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    // 1.5.0 版本开始可以直接利用 try-with-resources 特性
                    //支持Entry的自动close，close方法里最终会调用到exit方法
                    try (Entry entry = SphU.entry("HelloWorld2")) {
                        // 被保护的逻辑
                        System.out.println("hello world");
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (BlockException ex) {
                        // 处理被流控的逻辑
                        //com.alibaba.csp.sentinel.slots.block.flow.FlowException
                        ex.printStackTrace();
                        System.out.println("blocked!");
                    }
                }
            }).start();
        }
    }

    private static void initFlowRules(){
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld2");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(2);

        List<FlowRule> rules = new ArrayList<>();
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }


}
