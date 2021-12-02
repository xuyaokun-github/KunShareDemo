package cn.com.kun.controller.redisson;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.RedissonUtil;
import cn.com.kun.service.redisson.RedissonDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/redisson")
@RestController
public class RedissonDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedissonDemoController.class);

    @Autowired
    private RedissonDemoService redissonDemoService;

    @RequestMapping("/testLock10Thread")
    public String testLock10Thread(){

        redissonDemoService.testLock(10);
        return "cn.com.kun.controller.redisson.RedissonDemoController.testLock";
    }

    @RequestMapping("/testLockOneThread")
    public String testLockOneThread(){

        redissonDemoService.testLock(1);
        return "cn.com.kun.controller.redisson.RedissonDemoController.testLock";
    }

    @RequestMapping("/test2")
    public String test2(){

        RedissonUtil.setString("key", DateUtils.now(), 30);
        System.out.println(RedissonUtil.getString("key"));
        return "cn.com.kun.controller.redisson.RedissonDemoController.test2";
    }

    @RequestMapping("/test3")
    public String test3(){
        redissonDemoService.test2();
        return "cn.com.kun.controller.redisson.RedissonDemoController.test3";
    }

    @RequestMapping("/testString")
    public String testString(){
        redissonDemoService.testString();
        return "cn.com.kun.controller.redisson.RedissonDemoController.testString";
    }

}
