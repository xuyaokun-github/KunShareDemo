package cn.com.kun.component.scc.configrefresh.refreshedhandler;

import cn.com.kun.component.ratelimiter.RateLimiterHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 动态调整限流器的配置
 *
 * author:xuyaokun_kzx
 * date:2021/8/6
 * desc:
*/
@Component
public class RateLimiterSccConfigRefreshedHandler implements SccConfigRefreshedHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RateLimiterSccConfigRefreshedHandler.class);

    //这个是限流器容器的操作类，里面注入了RateLimiterProperties，
    // 一旦配置刷新，RateLimiterProperties的值就会改变
    @Autowired
    RateLimiterHolder rateLimiterHolder;

    @Override
    public void refreshed(Collection<String> refreshedKeyList) {
        /*
            这个实现层的逻辑：根据新的限流器配置，重新初始化限流器RateLimiter放到内存。
            有些配置的刷新就可以被使用了，无需再做这种解析处理就可以不实现SccConfigRefreshedHandler
         */

        if (hasConcernedKey(refreshedKeyList, "ratelimit")){
            LOGGER.info("重新加载限流器配置");
            rateLimiterHolder.resolveLimitConfig();
        }
    }


}
