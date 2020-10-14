package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuyaokun On 2020/10/14 22:56
 * @desc: 
 */
@RequestMapping("/spring-redis")
@RestController
public class SpringRedisDemocontroller {

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    System.out.println(generateUniqueId());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "" + i).start();
        }
        
        return "OK";
    }

    /**
     * 生成递增流水号（不要求连续）
     * @return
     */
    private String generateUniqueId(){
        //获取当前秒作为key
        String currentSecond = DateUtils.nowWithNoSymbol();
        RedisAtomicLong counter = new RedisAtomicLong(currentSecond, redisTemplate.getConnectionFactory());
        counter.expire(60, TimeUnit.SECONDS);//设置过期时间
        long number = counter.incrementAndGet();
        if (number > 999999){
            //限制位数（从自己系统的每秒下单数考虑）
            throw new RuntimeException("生成流水号失败！请稍后重试");
        }
        String id = currentSecond + fillString(number);
        return id;
    }

    /**
     * 填充字符串至6位
     */
    private String fillString(long number){
        String source = "" + number;
        while (source.length() < 6){
            source = "0" + source;
        }
        return source;
    }

}
