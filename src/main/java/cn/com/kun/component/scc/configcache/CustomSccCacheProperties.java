package cn.com.kun.component.scc.configcache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 这个属性定义在bootstrap文件
 *
 * author:xuyaokun_kzx
 * date:2021/8/9
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="custom.scc.cache")
public class CustomSccCacheProperties implements Serializable {

    private boolean enabled;

    private String filePath = new ApplicationHome().getDir().getAbsolutePath();

    private String path = filePath + "/local-config-cache.properties";

    /**
     * 使用本地缓存启动
     */
    private boolean useLocal;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUseLocal() {
        return useLocal;
    }

    public void setUseLocal(boolean useLocal) {
        this.useLocal = useLocal;
    }
}
