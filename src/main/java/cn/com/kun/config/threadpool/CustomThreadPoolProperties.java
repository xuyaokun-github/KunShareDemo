package cn.com.kun.config.threadpool;

import cn.com.kun.common.utils.JacksonUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * 限流的配置
 *
 * 配置文件内容例子：
 *
 *
 * author:xuyaokun_kzx
 * date:2021/6/30
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="custom.thread-pool")
public class CustomThreadPoolProperties implements Serializable {

    /**
     * 按场景进行限流配置
     * 假如是向后限流，会用在任何位置，限制本系统里的某个方法代码的执行频率
     * 常用场景：限制调用外部第三方接口的频率
     */
    private Map<String, ThreadPoolConfigItem> items;

    public Map<String, ThreadPoolConfigItem> getItems() {
        return items;
    }

    public void setItems(Map<String, ThreadPoolConfigItem> items) {
        this.items = items;
    }

    /**
     * 这个类必须是public static，否则会抛异常
     */
    public static class ThreadPoolConfigItem {

        private int corePoolSize;

        private int maxPoolSize;

        private int queueCapacity;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }


    @Override
    public String toString() {
        return JacksonUtils.toJSONString(items);
    }
}
