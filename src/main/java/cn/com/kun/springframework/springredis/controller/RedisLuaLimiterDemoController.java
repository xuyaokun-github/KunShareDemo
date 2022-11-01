package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.controller.HelloController;
import cn.com.kun.springframework.springredis.luaLimiter.RedisLuaLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 向后限流
 *
 * author:xuyaokun_kzx
 * date:2021/8/20
 * desc:
*/
@RequestMapping("/redis-lua-limit")
@RestController
public class RedisLuaLimiterDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    RedisLuaLimiter redisLuaLimiter;



    /**
     * 验证多线程并发不同的场景
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/testRateLimit")
    public ResultVo testRateLimit() throws InterruptedException {

        String key = "kunghsu-biz";
        for (int i = 0; i < 15; i++) {
            new Thread(()->{
                try {
                    if (redisLuaLimiter.accquire(key, 5)){
                        LOGGER.info("执行业务逻辑，线程：{}", Thread.currentThread().getName());
                    }else {
                        //未获取令牌
                        LOGGER.info("未获取令牌，线程：{}", Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return ResultVo.valueOfSuccess();
    }



}
