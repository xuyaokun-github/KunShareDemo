package cn.com.kun.component.memorycache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "memorycache")
public class MemoryCacheProperties {

    /**
     * 全局开关
     * 设置为false,禁用MemoryCache功能
     */
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
