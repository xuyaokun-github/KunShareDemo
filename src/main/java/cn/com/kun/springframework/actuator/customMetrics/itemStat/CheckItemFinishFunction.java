package cn.com.kun.springframework.actuator.customMetrics.itemStat;

@FunctionalInterface
public interface CheckItemFinishFunction<T> {

    /**
     * 检查是否完成（完成后就要移除监控）
     * @param value
     * @return
     */
    boolean check(T value);

}