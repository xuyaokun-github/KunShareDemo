package cn.com.kun.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 是否开启spring-cache机制的开关放这里，不用放到各个具体实现层去
 * 可以提供一个属性控制采取哪种cachemanager属性
 *
 * author:xuyaokun_kzx
 * date:2021/5/19
 * desc:
*/
@EnableCaching
@Configuration
public class CommonCacheConfig {


}
