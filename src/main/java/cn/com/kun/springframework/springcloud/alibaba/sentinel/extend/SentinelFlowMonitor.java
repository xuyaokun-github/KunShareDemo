package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.FlowMonitorRes;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 流量监控处理器
 *
 * author:xuyaokun_kzx
 * date:2021/9/30
 * desc:
*/
@Service
public class SentinelFlowMonitor {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelFlowMonitor.class);

    private static ExecutorService pool = Executors.newFixedThreadPool(
            1, new NamedThreadFactory("sentinel-flowMonitor", true));

    /**
     * 需要监控的所有context
     * 建议不要太多，增加监控的负担
     */
    private List<String> contextNameList = new ArrayList<>();

    /**
     * key ->
     * value -> FlowMonitorRes(装载着红、黄、绿三个状态的标识)
     */
    private Map<String, FlowMonitorRes> monitorFlagMap = new ConcurrentHashMap();

    /**
     * 绿黄分隔线具体值
     */
    private Map<String, Long> yellowLineThresholdMap = new ConcurrentHashMap<>();

    /**
     * 回调实例
     */
    private Map<String, FlowMonitorCallback> callbackMap = new ConcurrentHashMap<>();


//    @PostConstruct
    public void init(){

        //每隔一定的时间，就要进行判断
        //单线程监控
        //假如出现 拒绝QPS大于0，说明已经有某个线程开始出现限流了，这时候就可以将标识置为 红
        //假如 通过QPS大于某个值，但未到限流阶段，设置为黄
        //假如 低于某个值，设置为绿！
//        new Thread(new FlowMonitorTask()).start();

        pool.execute(new FlowMonitorTask());
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
     * 根据资源名称获取对应的 流量监控结果
     * （获取单个资源对应的结果）
     * @param resourceName
     * @return
     */
    public FlowMonitorRes getFlowMonitorRes(String resourceName) {

        return monitorFlagMap.get(resourceName);
    }


    /**
     * 获取多个资源对应的监控结果，合并成一个统一结果返回
     * 注意合并结果，只获取超过了阈值线的监控结果的数据，因为上层更关心的是超负荷情况
     *
     * @param resourceNames
     * @return
     */
    public FlowMonitorRes getMergeFlowMonitorRes(String... resourceNames) {

        FlowMonitorRes mergeRes = new FlowMonitorRes();
        mergeRes.setResource(String.join(",", resourceNames));
        mergeRes.markGreen();
        for (String resourceName : resourceNames) {
            //遍历所有资源对应的监控结果，合并成一个统一结果返回
            FlowMonitorRes flowMonitorRes = monitorFlagMap.get(resourceName);
            if (flowMonitorRes != null){
                if (flowMonitorRes.isRed()){
                    //打上红色标记并退出
                    mergeRes.markRed();
                    mergeRes.setResource(flowMonitorRes.getResource());
                    mergeRes.setTotalQps(flowMonitorRes.getTotalQps());
                    break;
                }
                if (!mergeRes.isYellow() && flowMonitorRes.isYellow()){
                    //运行到这里，假如mergeRes未被打上黄色 && 当前遍历元素为黄色，打上黄色标记
                    mergeRes.markYellow();
                    mergeRes.setResource(flowMonitorRes.getResource());
                    mergeRes.setTotalQps(flowMonitorRes.getTotalQps());
                    //这里是跳过，因为可能下一个资源是红色状态，所以仍需继续循环
                    continue;
                }
            }
        }
        return mergeRes;
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
     *  设置某个资源的黄色预警线
     *  假如没设置黄色预警线，则统一设置为绿色状态，这样就无法做到更平稳的控制
     *  例如，没设置黄色预警线，则无法针对 10% 和 95% 做进一步的区分对待，95%理应被标记为黄区
     * @param resourceName
     * @param yellowLineThreshold
     */
    public void registYellowLineThreshold(String resourceName, Long yellowLineThreshold){

        yellowLineThresholdMap.put(resourceName, yellowLineThreshold);
    }

    class FlowMonitorTask implements Runnable{

        @Override
        public void run() {

            while (true){
                try {
                    for (String contextName : contextNameList){
                        List<NodeVo> results = CustomFetchJsonTreeHandler.getJsonTreeForFixedContext(contextName);
                        if (results == null){
                            continue;
                        }
                        results.forEach(nodeVo -> {
                            String resourceName = nodeVo.getResource();
                            FlowMonitorRes flowMonitorRes = monitorFlagMap.get(resourceName);
                            if(flowMonitorRes == null){
                                flowMonitorRes = new FlowMonitorRes();
                                flowMonitorRes.setResource(resourceName);
                                monitorFlagMap.put(resourceName, flowMonitorRes);
                            }
                            //刷新MonitorFlag对象
                            refreshMonitorFlag(flowMonitorRes, nodeVo);
                            //触发回调
                            invokeMonitorCallback(flowMonitorRes);
                        });
                    }
                } catch (Exception e) {
                    LOGGER.error("FlowMonitorTask异常", e);
//                    e.printStackTrace();
                }finally {
                    //监控频率间隔1秒或者几秒，这个可以通过阈值设置
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        LOGGER.error("FlowMonitorTask InterruptedException", e);
                    }
                }
            }
        }

    }

    /**
     * 触发监控后回调
     * @param flowMonitorRes
     */
    private void invokeMonitorCallback(FlowMonitorRes flowMonitorRes) {

        FlowMonitorCallback flowMonitorCallback = callbackMap.get(flowMonitorRes.getResource());
        if (flowMonitorCallback != null){
            /*
                回调动作是由FlowMonitorProcessor组件完成，因此回调逻辑不能太重，不能有长耗时操作，否则会影响监控频率
             */
            flowMonitorCallback.monitorCallback(flowMonitorRes);
        }

    }

    /**
     * 刷新监控结果信息
     *
     * @param flowMonitorRes
     * @param nodeVo
     */
    private void refreshMonitorFlag(FlowMonitorRes flowMonitorRes, NodeVo nodeVo) {

        //一个NodeVo表示一个资源，但PassQps表示的是当个线程的值，BlockQps表示的所有线程的被拒绝的合计
        long totalQps = nodeVo.getTotalQps();
        flowMonitorRes.setTotalQps(totalQps);
        //判断是否达到红线
        long blockQps = nodeVo.getBlockQps();
        if (blockQps > 0){
            //拒绝Qps大于0，说明限流已经发生，置为红，表示当前系统已到高峰
            flowMonitorRes.markRed();
        }else {
            long passQps = nodeVo.getPassQps();
            Long greedYellowLineThreshold = yellowLineThresholdMap.get(nodeVo.getResource());
            if (greedYellowLineThreshold != null){
                if (passQps < greedYellowLineThreshold){
                    //小于某个值，就置为绿（这个值可以写死，也可以通过其他map来获取）
                    //这个值，其实就是绿区和黄区的分界线
                    flowMonitorRes.markGreen();
                }else {
                    //非绿即黄
                    //其他情况置为黄
                    flowMonitorRes.markYellow();
                }
            }else {
                flowMonitorRes.markGreen();
            }
        }
    }


}
