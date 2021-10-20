package cn.com.kun.springframework.springredis.counter;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.springframework.springredis.controller.RedisJobPriorityQueueController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.com.kun.common.utils.DateUtils.PATTERN_YYYY_MM_DD;

@Service
public class RedisCounterDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisJobPriorityQueueController.class);

    private String KEY_PREFIX = "redisCounter-msg-send";

    @Autowired
    private RedisCounter redisCounter;


    public void addRequest() {

        /**
         *  每天生成一个key，格式："redisCounter-msg-send" + 20211020
         *  过期时间设置为一天，让他自动过期，数据不会一直增多，这个也不会占太多数据量，是否过期不是重点
         */
        String key = KEY_PREFIX + DateUtils.toStr(new Date(), PATTERN_YYYY_MM_DD);
        long res = redisCounter.add(KEY_PREFIX, 1, 1, TimeUnit.DAYS);
        LOGGER.info("加一后：{}", res);
    }


}
