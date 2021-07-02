package cn.com.kun.springframework.springcloud.hystrix.hystrixExtend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * author:xuyaokun_kzx
 * date:2021/7/1
 * desc:
*/
@Component
public class HystrixRateLimitValueHolder {

    public final static Logger LOGGER = LoggerFactory.getLogger(HystrixRateLimitValueHolder.class);


    @Autowired
    private HystrixRateLimiterProperties hystrixRateLimiterProperties;

    private Map<String, String> bizSceneNameMap = new ConcurrentHashMap<>();

    private Map<String, String> itemNameMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        resolveLimitConfig();
    }

    /**
     * 解析限流配置内容，创建限流器
     */
    private void resolveLimitConfig() {

        Map<String, BusinessSceneLimit> businessSceneLimitMap = hystrixRateLimiterProperties.getBusinessSceneLimit();
        Iterator iterator = businessSceneLimitMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String bizSceneName = (String) entry.getKey();
            BusinessSceneLimit businessSceneLimit = (BusinessSceneLimit) entry.getValue();
            bizSceneNameMap.put(bizSceneName, businessSceneLimit.getDefaultRate());
            List<BusinessSceneLimit.LimitItem> limitItemList = businessSceneLimit.getLimitItem();
            limitItemList.forEach(limitItem -> {
                itemNameMap.put(bizSceneName + "-" + limitItem.getName(), limitItem.getRate());
            });
        }
    }

    /**
     * 根据业务场景名称和具体场景种类，获取限流值
     * @param bizSceneName
     * @param itemName
     * @return
     */
    public String getRateLimitValue(String bizSceneName, String itemName) {

        if (!bizSceneNameMap.containsKey(bizSceneName)){
            return null;
        }else {
            String key = bizSceneName + "-" + itemName;
            if (itemNameMap.containsKey(key)){
                return itemNameMap.get(key);
            }else {
                return bizSceneNameMap.get(bizSceneName);
            }
        }
    }

}
