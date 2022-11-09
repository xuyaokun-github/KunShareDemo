package cn.com.kun.component.monitor.worker;

/**
 * 监控逻辑抽象
 * author:xuyaokun_kzx
 * date:2021/11/29
 * desc:
*/
public interface MonitorWorker {

    /**
     * 监控逻辑
     * 如何监控&监控后应该做什么，由子类去实现
     * 监控后做的工作，假如是一个比较重的逻辑，建议异步执行，否则会影响监控组件的后续执行
     * 假如一直返回false，表示需要一直监控（这类就属于永久检测任务）
     *
     * @return 返回是否监控完成
     */
    boolean doMonitor();

}
