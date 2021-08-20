package cn.com.kun.component.ratelimiter.properties;

import cn.com.kun.component.ratelimiter.validate.RateLimitConfigValidation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Map;

/**
 * 限流的配置
 *
 * TODO 该类还可以配合@RefreshScope注解做限流配置的刷新
 * 访问/refresh接口，会触发该bean的刷新，然后定时启动一个线程不停地刷新重新构建限流器
 * 即可实现动态限流！
 *
 * author:xuyaokun_kzx
 * date:2021/6/30
 * desc:
*/
@RefreshScope
@Component
@ConfigurationProperties(prefix ="ratelimit")
@RateLimitConfigValidation
@Validated //必须加这个注解，自定义的@RateLimitConfigValidation才会生效
public class RateLimiterProperties implements Serializable {

    /**
     * 限流功能开关
     */
    private boolean enabled;

    /**
     * 默认全局限流值
     */
    private Double globalRate = Double.valueOf(200);

    /**
     * 按场景进行限流配置
     * 假如是向前限流，一般使用在控制层，限制外部系统调用本系统的频率
     */
    private Map<String, ForwardLimit> forward;

    /**
     * 按场景进行限流配置
     * 假如是向后限流，会用在任何位置，限制本系统里的某个方法代码的执行频率
     * 常用场景：限制调用外部第三方接口的频率
     */
    private Map<String, BizSceneLimit> backward;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Double getGlobalRate() {
        return globalRate;
    }

    public void setGlobalRate(Double globalRate) {
        this.globalRate = globalRate;
    }

    public Map<String, ForwardLimit> getForward() {
        return forward;
    }

    public void setForward(Map<String, ForwardLimit> forward) {
        this.forward = forward;
    }

    public Map<String, BizSceneLimit> getBackward() {
        return backward;
    }

    public void setBackward(Map<String, BizSceneLimit> backward) {
        this.backward = backward;
    }
}
