package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.MonitorFlag;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * 需要监控的所有context
     * 建议不要太多，增加监控的负担
     */
    private List<String> contextNameList = new ArrayList<>();

    /**
     * key ->
     * value -> MonitorFlag(装载着红、黄、绿三个状态的标识)
     */
    private Map<String, MonitorFlag> monitorFlagMap = new ConcurrentHashMap();

    /**
     * 绿黄分隔线具体值
     */
    private Map<String, Long> greedYellowLineThresholdMap = new ConcurrentHashMap<>();

    /**
     * 回调实例
     */
    private Map<String, FlowMonitorCallback> callbackMap = new ConcurrentHashMap<>();


    @PostConstruct
    public void init(){

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
     *
     * @param resourceName
     * @return
     */
    public MonitorFlag getFlowMonitorFlag(String resourceName) {

        return monitorFlagMap.get(resourceName);
    }

    /**
     * 注册需要监控的context
     * 未注册的不会获取QPS指标
     * @param contextName
     */
    public void registContextName(String contextName){
        contextNameList.add(contextName);
    }

    /**
     * 注册回调函数
     * @param resourceName
     * @param flowMonitorCallback
     */
    public void registFlowMonitorCallback(String resourceName, FlowMonitorCallback flowMonitorCallback){
        callbackMap.put(resourceName, flowMonitorCallback);
    }

    /**
     *  设置某个资源的绿黄分隔线
     *  假如没设置绿黄分隔线，则统一设置为绿，这样就无法做到更平稳的控制
     *  例如，没设置绿黄分割线，则无法针对 10% 和 95% 做进一步的区分对待，95%理应被标记为黄区
     * @param resourceName
     * @param greedYellowLineThreshold
     */
    public void registGreedYellowLineThreshold(String resourceName, Long greedYellowLineThreshold){

        greedYellowLineThresholdMap.put(resourceName, greedYellowLineThreshold);
    }

    class FlowMonitorTask implements Runnable{

        @Override
        public void run() {

            while (true){
                try {
                    for (String contextName : contextNameList){
                        List<NodeVo> results = CustomFetchJsonTreeHandler.getJsonTreeForFixedContext(contextName);
                        results.forEach(nodeVo -> {
                            String resourceName = nodeVo.getResource();
                            MonitorFlag monitorFlag = monitorFlagMap.get(resourceName);
                            if(monitorFlag == null){
                                monitorFlag = new MonitorFlag();
                                monitorFlag.setResource(resourceName);
                                monitorFlagMap.put(resourceName, monitorFlag);
                            }
                            //刷新MonitorFlag对象
                            refreshMonitorFlag(monitorFlag, nodeVo);
                            //触发回调
                            invokeMonitorCallback(monitorFlag);
                        });
                    }

                    //监控频率间隔1秒或者几秒，这个可以通过阈值设置
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 触发监控后回调
     * @param monitorFlag
     */
    private void invokeMonitorCallback(MonitorFlag monitorFlag) {

        FlowMonitorCallback flowMonitorCallback = callbackMap.get(monitorFlag.getResource());
        if (flowMonitorCallback != null){
            /*
                回调动作是由FlowMonitorProcessor组件完成，因此回调逻辑不能太重，不能有长耗时操作，否则会影响监控频率
             */
            flowMonitorCallback.monitorCallback(monitorFlag);
        }

    }

    /**
     * 刷新监控结果信息
     *
     * @param monitorFlag
     * @param nodeVo
     */
    private void refreshMonitorFlag(MonitorFlag monitorFlag, NodeVo nodeVo) {

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
            Long greedYellowLineThreshold = greedYellowLineThresholdMap.get(nodeVo.getResource());
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
    }


}
