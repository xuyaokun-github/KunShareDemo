package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.springredis.service.RedisLargeCacheContainerDemoService;
import cn.com.kun.springframework.springredis.vo.UserRedisVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RequestMapping("/spring-redis-largeCacheContainer")
@RestController
public class RedisLargeCacheContainerDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRedisDemocontroller.class);

    @Autowired
    RedisLargeCacheContainerDemoService redisLargeCacheContainerDemoService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request){

        redisLargeCacheContainerDemoService.delete();

        for (int i = 0; i < 10; i++) {
            UserRedisVO userRedisVO = new UserRedisVO();
            userRedisVO.setUsername(UUID.randomUUID().toString());
            userRedisVO.setCustomerType("1");
            userRedisVO.setPhone(UUID.randomUUID().toString());
            userRedisVO.setCustomerId(UUID.randomUUID().toString());
            redisLargeCacheContainerDemoService.save(userRedisVO);
        }

        UserRedisVO userRedisVO = new UserRedisVO();
        userRedisVO.setCustomerType("1");
        userRedisVO.setCustomerId("kunghsu");
        userRedisVO.setUsername(UUID.randomUUID().toString());
        userRedisVO.setPhone(UUID.randomUUID().toString());
        redisLargeCacheContainerDemoService.save(userRedisVO);
        userRedisVO.setPhone("10086");
        userRedisVO.setUsername(null);
        redisLargeCacheContainerDemoService.update(userRedisVO);
        long count = redisLargeCacheContainerDemoService.size();

        LOGGER.info("size:{}", count);

        //get操作
        String queryKey = "1:kunghsu";
        UserRedisVO userRedisVO1 = redisLargeCacheContainerDemoService.get(queryKey);
        LOGGER.info("UserRedisVO Res:{}", JacksonUtils.toJSONString(userRedisVO1));

        return "OK";
    }


}
