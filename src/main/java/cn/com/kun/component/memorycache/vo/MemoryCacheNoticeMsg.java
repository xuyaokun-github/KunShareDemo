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

    /**
     * 缓存器名称
     */
    private String configName;

    /**
     * 业务key
     */
    private String bizKey;

    /**
     * 更新时间戳
     */
    private String updateTimemillis;

    public MemoryCacheNoticeMsg() {
    }

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
