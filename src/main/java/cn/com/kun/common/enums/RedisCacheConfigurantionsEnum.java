package cn.com.kun.common.enums;

import java.time.Duration;

/**
 * 各个服务层的redis缓存管理器配置定义在这个类
 *
 * author:xuyaokun_kzx
 * date:2021/6/25
 * desc:
*/
public enum RedisCacheConfigurantionsEnum {

    /**
     * 每次新增一个服务层需要用redis缓存，就需要加一项枚举定义
     */
    STUDENT("student-service:", Duration.ofDays(5), "student-service"),
    USER("user-service:", Duration.ofDays(1), "user-service");

    /**
     * key的前缀
     */
    public String prefixKey;

    /**
     * 过期时间
     */
    public Duration entryTtl;

    /**
     * 每个模块的缓存管理器名，用于区分不同的缓存管理器
     */
    public String configName;

    RedisCacheConfigurantionsEnum(String prefixKey, Duration entryTtl, String configName) {
        this.prefixKey = prefixKey;
        this.entryTtl = entryTtl;
        this.configName = configName;
    }

    public String getPrefixKey() {
        return prefixKey;
    }

    public void setPrefixKey(String prefixKey) {
        this.prefixKey = prefixKey;
    }

    public Duration getEntryTtl() {
        return entryTtl;
    }

    public void setEntryTtl(Duration entryTtl) {
        this.entryTtl = entryTtl;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
