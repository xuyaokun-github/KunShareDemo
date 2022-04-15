package cn.com.kun.springframework.actuator.customMetrics.batchJobExecTimeStat;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配合业务监控使用--基于Guage统计批处理运行时间
 * 无论采集频率和任务执行频率哪个更快，都可以采集，只是采集的没有Timer准确，但是Timer展示出来的图表无法反馈具体执行了多少次
 * (设计废弃)
 *
 * author:xuyaokun_kzx
 * date:2022/4/11
 * desc:
*/
@Component
public class BatchJobExecTimeStatHelperByQueue {

    /**
     * 用队列存，有隐患，假如一直没有出队，队列会越来越大
     */
    private Map<String, LinkedList<Double>> execTimeMap = new ConcurrentHashMap<>();


    /**
     * 在任务结束后记录执行时间
     * @param jobName
     * @param execTimeMs
     */
    public void recordExecTime(String jobName, Long execTimeMs){

        LinkedList<Double> linkedList = execTimeMap.get(jobName);
        if (linkedList == null){
            linkedList = new LinkedList<>();
            execTimeMap.put(jobName, linkedList);
        }
        linkedList.push(execTimeMs.doubleValue());
    }

    /**
     * 假如获取到0，说明尚未执行，应该返回0
     * 这里有个问题，假如采集功能崩溃了，且一直没恢复，弹出动作相当于停止了，则队列的元素会越来越多，这是一个隐患
     *
     * @param jobName
     * @return
     */
    public double value(String jobName) {

        LinkedList<Double> linkedList = execTimeMap.get(jobName);
        if (linkedList != null){
            Double execTime = linkedList.pollFirst();
            if (execTime != null){
                //每次采集完，都将之前的数据清空，牺牲了某些执行的耗时记录，但不影响整体统计
//                linkedList.clear();
                return execTime;
            }
        }else {
            execTimeMap.put(jobName, new LinkedList<>());
        }

        return 0;

    }

}
