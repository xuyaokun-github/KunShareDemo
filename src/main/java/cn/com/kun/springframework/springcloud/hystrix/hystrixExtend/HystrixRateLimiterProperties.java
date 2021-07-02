package cn.com.kun.springframework.springcloud.hystrix.hystrixExtend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * 限流的配置
 *
 * author:xuyaokun_kzx
 * date:2021/6/30
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="hystrix-ratelimit")
public class HystrixRateLimiterProperties implements Serializable {

    /**
     * 限流功能开关
     */
    private boolean enabled;

    /**
     * 按场景进行限流配置
     * 假如是向后限流，会用在任何位置，限制本系统里的某个方法代码的执行频率
     * 常用场景：限制调用外部第三方接口的频率
     */
    private Map<String, BusinessSceneLimit> businessSceneLimit;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, BusinessSceneLimit> getBusinessSceneLimit() {
        return businessSceneLimit;
    }

    public void setBusinessSceneLimit(Map<String, BusinessSceneLimit> businessSceneLimit) {
        this.businessSceneLimit = businessSceneLimit;
    }
}
