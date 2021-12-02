package cn.com.kun.component.monitor;

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
     * 假如一直返回false，表示需要一直监控
     *
     * @return 返回是否监控完成
     */
    boolean doMonitor();
}
