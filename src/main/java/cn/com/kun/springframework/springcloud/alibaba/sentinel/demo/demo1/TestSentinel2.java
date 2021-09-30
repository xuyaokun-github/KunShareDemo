package cn.com.kun.springframework.springcloud.alibaba.sentinel.demo.demo1;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑一下代码，然后观察下输出，QPS 数据在 50~100 这个区间一直变化，印证了我前面说的，秒级 QPS 统计是极度不准确的。
 * author:xuyaokun_kzx
 * date:2021/9/30
 * desc:
*/
public class TestSentinel2 {

    public static void main(String[] args) {
        // 下面几行代码设置了 QPS 阈值是 100
        FlowRule rule = new FlowRule("test");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(100);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        List<FlowRule> list = new ArrayList<>();
        list.add(rule);
        FlowRuleManager.loadRules(list);

        // 先通过一个请求，让 clusterNode 先建立起来
        try (Entry entry = SphU.entry("test")) {
        } catch (BlockException e) {
        }

        // 起一个线程一直打印 qps 数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(ClusterBuilderSlot.getClusterNode("test").passQps());
                }
            }
        }).start();

        while (true) {
            try (Entry entry = SphU.entry("test")) {
                Thread.sleep(5);
            } catch (BlockException e) {
                // ignore
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
