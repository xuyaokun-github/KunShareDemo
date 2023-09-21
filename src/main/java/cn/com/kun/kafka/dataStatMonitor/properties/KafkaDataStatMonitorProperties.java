package cn.com.kun.kafka.dataStatMonitor.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/29
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kafka.data-stat")
public class KafkaDataStatMonitorProperties implements Serializable {

    private boolean dataReportEnabled;

    private boolean lagCheckEnabled;

    /**
     * 正常的监控时间间隔：默认是1分钟
     * 假如用户定义了大于0的值，以用户定义的为准
     */
    private long normalTimeIntervalMs;

    public boolean isDataReportEnabled() {
        return dataReportEnabled;
    }

    public void setDataReportEnabled(boolean dataReportEnabled) {
        this.dataReportEnabled = dataReportEnabled;
    }

    public boolean isLagCheckEnabled() {
        return lagCheckEnabled;
    }

    public void setLagCheckEnabled(boolean lagCheckEnabled) {
        this.lagCheckEnabled = lagCheckEnabled;
    }

    public long getNormalTimeIntervalMs() {
        return normalTimeIntervalMs;
    }

    public void setNormalTimeIntervalMs(long normalTimeIntervalMs) {
        this.normalTimeIntervalMs = normalTimeIntervalMs;
    }
}
