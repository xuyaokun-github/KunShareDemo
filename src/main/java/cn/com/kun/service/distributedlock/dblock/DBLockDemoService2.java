package cn.com.kun.service.distributedlock.dblock;

import cn.com.kun.component.distributedlock.dblock.DistributedDbLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 数据库锁的测试demo,长时间运行，观察数据库连接数等是否有问题
 * 只在测试环境使用
 * author:xuyaokun_kzx
 * date:2022/9/16
 * desc:
*/
@Service
public class DBLockDemoService2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBLockDemoService2.class);

    String resourceName = "cn.com.kun.service.distributedlock.dblock.DBLockDemoService2.test";

    @Autowired
    DistributedDbLock dbClusterLockHandler;

    @Autowired
    private RedisTemplate redisTemplate;

    public void test() throws InterruptedException {

        redisTemplate.delete("dblock-demo-redis-key-producer");
        redisTemplate.delete("dblock-demo-redis-key-consumer");

        //start一个线程生产
        new Thread(()->{
            while (true){
                try {
                    //
                    Thread.sleep(3000);
//                    redisTemplate.opsForList().leftPush("dblock-demo-redis-key-list", UUID.randomUUID().toString());
                    redisTemplate.opsForValue().set("dblock-demo-redis-key-string", UUID.randomUUID().toString());
                    //生产，计数器加一
                    redisTemplate.opsForValue().increment("dblock-demo-redis-key-producer", 1);
                }catch (Exception e){

                }
            }
        }).start();

        //开十个线程抢锁进行消费
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    try {
                        //
                        Thread.sleep(1000);

//                        doConsume();
                        doConsumeByLock();
                    }catch (Exception e){

                    }
                }
            }).start();
        }

        //开启一个监控线程，每隔5秒输出一次 计数器情况
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //
                Object obj1 = redisTemplate.opsForValue().get("dblock-demo-redis-key-consumer");
                Object obj2 = redisTemplate.opsForValue().get("dblock-demo-redis-key-producer");
                LOGGER.info("dblock-demo-redis-key-consumer：{} dblock-demo-redis-key-producer：{}", obj1, obj2);
            }
        }).start();
    }

    /**
     * 不上锁
     */
    private void doConsume() {
//                        Object res = redisTemplate.opsForList().leftPop("dblock-demo-redis-key-list");
        Object res = redisTemplate.opsForValue().get("dblock-demo-redis-key-string");
        if (res != null && StringUtils.isNotEmpty((String)res)){
            //消费，计数器加一
            redisTemplate.opsForValue().increment("dblock-demo-redis-key-consumer", 1);
            redisTemplate.opsForValue().set("dblock-demo-redis-key-string", "");
        }
    }

    /**
     * 上锁
     */
    private void doConsumeByLock() {

        dbClusterLockHandler.lock(resourceName);
        try {
            doConsume();
        }finally {
            dbClusterLockHandler.unlock(resourceName);
        }
    }
}
