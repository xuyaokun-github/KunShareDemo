package cn.com.kun.springframework.springredis.luaLimiter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * author:xuyaokun_kzx
 * date:2022/11/1
 * desc:
*/
@Component
public class RedisLuaLimiter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisLuaLimiter.class);

    private String script;

    @Autowired
    RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        script = loadScript("redis/limiter.lua");
    }

    public boolean accquire(String key, int limit) throws Exception {

        List<String> keys = new ArrayList<String>();
        keys.add(key);
        Assert.notNull(script,"redis限流脚本为空");
        DefaultRedisScript<Object> defaultRedisScript = new DefaultRedisScript<>(script, Object.class);
        //这样接收结果是错的，返回的是list类型
//        Long result = (Long) redisTemplate.execute(defaultRedisScript, keys, limit);
        Long result = 1L;//默认放行
        Object object = redisTemplate.execute(defaultRedisScript, keys, limit);
        if (object instanceof ArrayList){
            result = (Long) ((ArrayList<?>) object).get(0);
        }else {
            result = (Long) object;
        }

        return result >= 1;
    }

    public String loadScript(String pathName){

        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource(pathName).getInputStream();
            //拿到输入流先转成字节
            byte[] b = IOUtils.toByteArray(inputStream);
            String script = new String(b, StandardCharsets.UTF_8);
            return script;
        } catch (IOException e) {
            LOGGER.info("加载Redis Lua限流脚本出错", e);
        }
        return null;
    }

}
