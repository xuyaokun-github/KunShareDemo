package cn.com.kun.service.redisson;

import cn.com.kun.common.config.redisson.RedissonAutowired;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.RedissonUtil;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonDemoService {

    /**
     * 基于注解自动注入Redisson对象
     */
    @RedissonAutowired
    private RLock rLock;

    public void test(){
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    rLock.lock();
                    try{
                        System.out.println(Thread.currentThread().getName() + DateUtils.now());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    } finally {
                        rLock.unlock();
                    }

                }
            },"redisson-demo-thread-" + i).start();
        }
    }

    /**
     * 模拟多个线程竞争，生成流水号（验证是否唯一）
     */
    public void test2(){
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
            },"redisson-demo-thread-" + i).start();
        }
    }

    /**
     * 基于Redisson生成递增流水号（不要求连续）
     * @return
     */
    private String generateUniqueId(){
        //获取当前秒作为key
        String currentSecond = DateUtils.nowWithNoSymbol();
        RAtomicLong rAtomicLong = RedissonUtil.getRAtomicLong(currentSecond);
        rAtomicLong.expire(60, TimeUnit.SECONDS);//设置过期时间
        long number = rAtomicLong.incrementAndGet();
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
