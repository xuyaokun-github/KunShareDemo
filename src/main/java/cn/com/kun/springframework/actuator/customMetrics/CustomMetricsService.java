package cn.com.kun.springframework.actuator.customMetrics;

/**
 * 自定义业务监控
 * author:xuyaokun_kzx
 * date:2021/11/24
 * desc:
*/
public interface CustomMetricsService {

    void gather(String counterName, String... values);
}
