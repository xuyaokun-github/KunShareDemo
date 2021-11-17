package cn.com.kun.component.repeatcheck.impl;

import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisRepeatCheckProcessor extends RepeatCheckSupport{

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisRepeatCheckProcessor.class);

    /**
     * 默认半小时：30 * 60
     */
    @Value("${repeatCheck.keepAliveSeconds:1800}")
    private long keepAliveSeconds;

    /**
     * 防重组件是否开启
     */
    @Value("${repeatCheck.enabled:true}")
    private boolean enabled;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Override
    public boolean checkRepeat(String key) {
        if (enabled){
            return false;
        }
        return redisTemplateHelper.hasKey(key);
    }

    @Override
    public boolean add(String key) {
        if (enabled){
            return false;
        }
        return redisTemplateHelper.set(key, "", keepAliveSeconds);
    }

}
