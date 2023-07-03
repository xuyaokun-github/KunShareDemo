package cn.com.kun.springframework.springredis.controller;


import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springredis.repeatCheck.RedisRepeatChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/spring-redis-repeatChecker")
@RestController
public class RedisRepeatCheckerDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisRepeatCheckerDemoController.class);

    @Autowired
    RedisRepeatChecker redisRepeatChecker;

    @GetMapping(value = "/test")
    public ResultVo test(){

        String uuid = UUID.randomUUID().toString();
//        LOGGER.info("{}", redisRepeatChecker.check(uuid));
//        LOGGER.info("{}", redisRepeatChecker.check(uuid));

        return ResultVo.valueOfSuccess("");
    }

}
