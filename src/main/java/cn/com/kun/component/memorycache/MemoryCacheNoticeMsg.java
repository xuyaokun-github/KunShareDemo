package cn.com.kun.component.memorycache;

import java.io.Serializable;

/**
 * 内存缓存清除通知消息
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
public class MemoryCacheNoticeMsg implements Serializable {

    private String configName;

    private String key;

    private String updateTimemillis;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUpdateTimemillis() {
        return updateTimemillis;
    }

    public void setUpdateTimemillis(String updateTimemillis) {
        this.updateTimemillis = updateTimemillis;
    }
}
