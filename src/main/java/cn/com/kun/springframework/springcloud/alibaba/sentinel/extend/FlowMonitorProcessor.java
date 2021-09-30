package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.MonitorFlag;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME;

/**
 * 流量监控处理器
 *
 * author:xuyaokun_kzx
 * date:2021/9/30
 * desc:
*/
@Service
public class FlowMonitorProcessor {

    /**
     * key ->
     * value -> MonitorFlag(装载着红、黄、绿三个状态的标识)
     */
    private Map<String, MonitorFlag> monitorFlagMap = new ConcurrentHashMap();

    /**
     * 绿黄分隔线具体值
     */
    private Map<String, Long> greedYellowLineThresholdMap = new HashMap<>();

    @PostConstruct
    public void init(){

        //设置某个资源的绿黄分隔线 为 3Tps（可以从配置文件读取）
        //假如没设置，就给个默认值，或者默认取QPS阈值的一半
        greedYellowLineThresholdMap.put(RESOURCE_NAME, 3L);

        //每隔一定的时间，就要进行判断
        //单线程监控
        //假如出现 拒绝QPS大于0，说明已经有某个线程开始出现限流了，这时候就可以将标识置为 红
        //假如 通过QPS大于某个值，但未到限流阶段，设置为黄
        //假如 低于某个值，设置为绿！
        new Thread(new FlowMonitorTask()).start();

        //kafka消费线程可以根据 红黄绿标识，自己判断是否需要加大睡眠时间，从而调整消费速度
        /*
            有三组kafka消费线程，高优先级线程、中优先级线程、低优先级线程
            假如到红状态，中优先级线程和低优先级，将消费速度拉到最慢或者甚至停止消费
            停止消费就有个缺点，导致消息始终无法得到处理
            假如到黄状态，中优先级线程和低优先级就将消费速度拉到 中等速度
            假如是绿状态，表明系统无太大压力，可以将三个级别的消费线程的消费速度拉到最高
            整个过程中，因为高优先级的必须要尽快得到处理，所以不能降低它的速度，只能让它一直拉
         */


    }

    /**
     * 根据资源名称获取对应的 流量监控标记
     * @param resourceName
     * @return
     */
    public MonitorFlag getFlowMonitorFlag(String resourceName) {

        return monitorFlagMap.get(resourceName);
    }

    class FlowMonitorTask implements Runnable{

        @Override
        public void run() {

            while (true){

                List<NodeVo> results = MyFetchJsonTreeHandler.getJsonTreeForSentinelDefaultContext();
                results.forEach(nodeVo -> {
                    String resourceName = nodeVo.getResource();
                    MonitorFlag monitorFlag = monitorFlagMap.get(resourceName);
                    if(monitorFlag == null){
                        monitorFlag = new MonitorFlag();
                        monitorFlagMap.put(resourceName, monitorFlag);
                    }
                    //一个NodeVo表示一个资源，但PassQps表示的是当个线程的值，BlockQps表示的所有线程的被拒绝的合计
                    long totalQps = nodeVo.getTotalQps();
                    monitorFlag.setTotalQps(totalQps);
                    //判断是否达到红线
                    long blockQps = nodeVo.getBlockQps();
                    if (blockQps > 0){
                        //拒绝Qps大于0，说明限流已经发生，置为红，表示当前系统已到高峰
                        monitorFlag.markRed();
                    }else {
                        long passQps = nodeVo.getPassQps();
                        Long greedYellowLineThreshold = greedYellowLineThresholdMap.get(resourceName);
                        if (greedYellowLineThreshold != null){
                            if (passQps < greedYellowLineThreshold){
                                //小于某个值，就置为绿（这个值可以写死，也可以通过其他map来获取）
                                //这个值，其实就是绿区和黄区的分界线
                                monitorFlag.markGreen();
                            }else {
                                //非绿即黄
                                //其他情况置为黄
                                monitorFlag.markYellow();
                            }
                        }else {
                            monitorFlag.markGreen();
                        }
                    }

                });
                try {
                    //间隔1秒或者几秒，这个可以通过阈值判断
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }



}
