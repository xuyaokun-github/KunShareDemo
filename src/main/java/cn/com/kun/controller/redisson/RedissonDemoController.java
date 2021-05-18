package cn.com.kun.controller.redisson;

import cn.com.kun.service.redisson.RedissonDemoService;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/redisson")
@RestController
public class RedissonDemoController {

    @Autowired
    private RedissonDemoService redissonDemoService;

    @RequestMapping("/test")
    public String test(){

        redissonDemoService.test();
        return "cn.com.kun.controller.redisson.RedissonDemoController.test";
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
