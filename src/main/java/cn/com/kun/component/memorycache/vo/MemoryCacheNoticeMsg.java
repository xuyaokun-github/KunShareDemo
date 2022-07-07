package cn.com.kun.component.memorycache.vo;

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

    private String bizKey;

    private String updateTimemillis;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getUpdateTimemillis() {
        return updateTimemillis;
    }

    public void setUpdateTimemillis(String updateTimemillis) {
        this.updateTimemillis = updateTimemillis;
    }
}
