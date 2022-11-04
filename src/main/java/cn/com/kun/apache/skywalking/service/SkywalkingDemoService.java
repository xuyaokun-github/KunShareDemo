package cn.com.kun.apache.skywalking.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 实现黑名单的功能-用什么数据结构，视需求决定
 *
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
@Service
public class SkywalkingDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SkywalkingDemoService.class);


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    SkywalkingDemoService2 skywalkingDemoService2;

    @Autowired
    SkywalkingDemoService22 skywalkingDemoService22;

    public void method1() throws InterruptedException {

        String replace = StringUtils.replace("oldString", "old","replaced");
        LOGGER.info(replace);
        //模拟一个耗时
        Thread.sleep(1000);
        //访问redis(这里有一个Redis操作，我想拦截Redis的某一个环节执行耗时)
        //例如org.springframework.data.redis.core.DefaultValueOperations.get(java.lang.Object) 可以写插件去抓耗时
        redisTemplate.opsForValue().get("kunghsu");
        //访问数据库
        skywalkingDemoService2.method1();
        skywalkingDemoService2.method2();
        skywalkingDemoService2.method3();
        skywalkingDemoService2.method4();
        skywalkingDemoService22.method4();
    }
}
