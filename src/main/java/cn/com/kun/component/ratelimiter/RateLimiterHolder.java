package cn.com.kun.component.ratelimiter;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.ratelimiter.properties.*;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流器管理类--保存所有限流器
 *
 * author:xuyaokun_kzx
 * date:2021/7/1
 * desc:
*/
@Component
public class RateLimiterHolder {

    public final static Logger LOGGER = LoggerFactory.getLogger(RateLimiterHolder.class);


    @Autowired
    private RateLimiterProperties rateLimiterProperties;

    /**
     * 全局限流器-每秒产生100个令牌
     */
    private RateLimiter GLOBAL_RATE_LIMITER = RateLimiter.create(100);

    private Map<String, RateLimiter> bizSceneRateLimiterMap = new ConcurrentHashMap();

    private Map<String, RateLimiter> itemLimiterMap = new ConcurrentHashMap();

    private Map<String, RateLimiter> forwardLimiterMap = new ConcurrentHashMap();

    private Map<String, Map<String, RateLimiter>> forwardLimitApiMap = new ConcurrentHashMap();

    @PostConstruct
    public void init(){
        GLOBAL_RATE_LIMITER = RateLimiter.create(rateLimiterProperties.getGlobalRate());
        resolveLimitConfig();
    }

    /**
     * 解析限流配置内容，创建限流器
     */
    public void resolveLimitConfig() {

        Map<String, BizSceneLimit> bizSceneLimitMap = rateLimiterProperties.getBackward();
        LOGGER.info("bizSceneLimitMap:{}", JacksonUtils.toJSONString(bizSceneLimitMap));
        if (bizSceneLimitMap != null){
            Iterator iterator = bizSceneLimitMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();//业务场景名称
                BizSceneLimit bizSceneLimit = (BizSceneLimit) entry.getValue();
                //创建限流器放入map容器，后续供限流判断使用
                bizSceneRateLimiterMap.put(key, RateLimiter.create(bizSceneLimit.getDefaultRate()));

                List<LimitItem> limitItemList = bizSceneLimit.getLimitItem();
                limitItemList.forEach(limitItem -> {
                    itemLimiterMap.put(key + "-" +limitItem.getName(), RateLimiter.create(limitItem.getRate()));
                });
            }
        }


        Map<String, ForwardLimit> forwardLimitMap = rateLimiterProperties.getForward();
        LOGGER.info("forwardLimitMap:{}", JacksonUtils.toJSONString(forwardLimitMap));
        if (forwardLimitMap != null){
            Iterator iterator2 = forwardLimitMap.entrySet().iterator();
            while (iterator2.hasNext()){
                Map.Entry entry = (Map.Entry) iterator2.next();
                String controllerName = (String) entry.getKey();//控制器名称
                ForwardLimit forwardLimit = (ForwardLimit) entry.getValue();
                //创建限流器放入map容器，后续供限流判断使用
                forwardLimiterMap.put(controllerName, RateLimiter.create(forwardLimit.getDefaultRate()));

                List<ApiLimit> apiLimitList = forwardLimit.getApiLimit();
                Map<String, RateLimiter> rateLimiterMap = new HashMap<>();
                apiLimitList.forEach(apiLimit -> {
                    rateLimiterMap.put(apiLimit.getApi(), RateLimiter.create(apiLimit.getApiRate()));
                });
                forwardLimitApiMap.put(controllerName, rateLimiterMap);
            }
        }

    }

    /**
     * 选择向后限流器
     * @param bizSceneName
     * @param itemName
     * @return
     */
    public RateLimiter chooseBackwardRateLimiter(String bizSceneName, String itemName) {

        if (!rateLimiterProperties.isEnabled()){
            //假如限流功能未开启，直接返回null,无需限流
            return null;
        }
        /*
            如何选择限流器?
            先判断是否有具体子场景，假如有，则返回具体场景的限流器，否则用业务场景的默认限流器
         */
        if (StringUtils.isEmpty(bizSceneName)){
            //假如业务场景为空，则返回默认的全局限流器
            return GLOBAL_RATE_LIMITER;
        }else {
            String key = bizSceneName + "-" + itemName;
            RateLimiter rateLimiter = itemLimiterMap.get(key);
            if (rateLimiter != null){
                //假如存在子场景对应的限流器，直接返回
                return rateLimiter;
            }else {
                //否则，返回业务场景默认的限流器
                return bizSceneRateLimiterMap.get(bizSceneName);
            }
        }
    }

    /**
     * 选择向前限流器
     * @param controllerName
     * @param uri
     * @return
     */
    public RateLimiter chooseForwardRateLimiter(String controllerName, String uri) {

        //假如没配controllerName，直接返回全局的限流配置
        if (StringUtils.isEmpty(controllerName)){
            return GLOBAL_RATE_LIMITER;
        }else {
            Map<String, RateLimiter> rateLimiterMap = forwardLimitApiMap.get(controllerName);
            Iterator iterator = rateLimiterMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                String api = (String) entry.getKey();
                if (uri.contains(api)){
                    return (RateLimiter) entry.getValue();
                }
            }
        }
        return GLOBAL_RATE_LIMITER;
    }

}
