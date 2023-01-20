package cn.com.kun.common.enums;


import static cn.com.kun.common.constants.MemoryCacheConfigConstants.MEMORY_CACHE_CONFIG_NAME_STUDENT;
import static cn.com.kun.common.constants.MemoryCacheConfigConstants.MEMORY_CACHE_CONFIG_NAME_STUDENT_2;

/**
 * 各个服务层的内存缓存管理器配置定义在这个类
 *
 * author:xuyaokun_kzx
 * date:2021/6/25
 * desc:
*/
public enum MemoryCacheConfigurantionsEnum {

    /**
     * 每次新增一个服务层需要用redis缓存，就需要加一项枚举定义
     */
    STUDENT(30*60, 1000, MEMORY_CACHE_CONFIG_NAME_STUDENT),
    STUDENT2(30*60, 1000, MEMORY_CACHE_CONFIG_NAME_STUDENT_2),
    USER(12000, 2000, "memory-cache-user-service");

    public long ttl;

    public long maximumSize;

    /**
     * 每个模块的缓存管理器名，用于区分不同的缓存管理器
     */
    public String configName;

    MemoryCacheConfigurantionsEnum(long ttl, long maximumSize, String configName) {
        this.ttl = ttl;
        this.maximumSize = maximumSize;
        this.configName = configName;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
