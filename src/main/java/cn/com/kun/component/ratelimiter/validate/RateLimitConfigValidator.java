package cn.com.kun.component.ratelimiter.validate;

import cn.com.kun.component.ratelimiter.properties.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RateLimitConfigValidator implements ConstraintValidator<RateLimitConfigValidation, RateLimiterProperties> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RateLimitConfigValidator.class);

    @Override
    public void initialize(RateLimitConfigValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(RateLimiterProperties rateLimiterProperties, ConstraintValidatorContext constraintValidatorContext) {

        if (!rateLimiterProperties.isEnabled()){
            //无需验证
            LOGGER.info("限流功能未开启，无需验证");
            return true;
        }

        if (rateLimiterProperties.getGlobalRate() == null || (rateLimiterProperties.getGlobalRate().compareTo(0D) < 1)){
            LOGGER.error("限流配置验证失败，全局限流值必须配置且大于0，请检查");
            return false;
        }

        //校验向前限流的配置
        Map<String, ForwardLimit>  forwardLimitMap = rateLimiterProperties.getForward();
        Iterator iterator;
        if (forwardLimitMap != null){
            iterator = forwardLimitMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                String controllerName = (String) entry.getKey();
                ForwardLimit forwardLimit = (ForwardLimit) entry.getValue();
                List<ApiLimit> apiLimitList = forwardLimit.getApiLimit();
                for (ApiLimit apiLimit : apiLimitList){
                    if(StringUtils.isEmpty(apiLimit.getApi()) || apiLimit.getApiRate() == null){
                        //api和apiRate不能为空
                        LOGGER.error("限流配置验证失败，控制器[{}]的配置有误，api和apiRate必须非空，请检查", controllerName);
                        return false;
                    }
                }
            }
        }


        //校验向后限流的配置
        Map<String, BizSceneLimit>  backwardRateLimit = rateLimiterProperties.getBackward();
        if (backwardRateLimit != null){
            iterator = backwardRateLimit.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                String controllerName = (String) entry.getKey();
                BizSceneLimit bizSceneLimit = (BizSceneLimit) entry.getValue();
                List<LimitItem> limitItemList = bizSceneLimit.getLimitItem();
                for (LimitItem limitItem : limitItemList){
                    if(StringUtils.isEmpty(limitItem.getName()) || limitItem.getRate() == null){
                        //api和apiRate不能为空
                        LOGGER.error("限流配置验证失败，场景[{}]的配置有误，name和rate必须非空，请检查", controllerName);
                        return false;
                    }
                }
            }
        }


        LOGGER.info("限流配置验证成功！！");
        return true;
    }


}
