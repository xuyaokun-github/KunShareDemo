package cn.com.kun.controller.distributedlock;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.distributedlock.redislock.RedisDistributedLockHandler;
import cn.com.kun.component.distributedlock.redislock.RedisLockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/redisClusterLock-demo")
@RestController
public class RedisClusterLockDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisClusterLockDemoController.class);

    @Autowired
    RedisLockUtil redisLockUtil;

    @Autowired
    RedisDistributedLockHandler redisClusterLockHandler;

    /**
     * http://localhost:8080/kunsharedemo/redisClusterLock-demo/test
     * @return
     */
    @GetMapping("/test")
    public ResultVo<String> test(){
        String lockKey = "redisClusterLock-lockkey";
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                String requestId = UUID.randomUUID().toString();
                //上锁
                for (;;){
                    boolean isGetLock = redisLockUtil.getLock(lockKey, requestId, 0);
                    if (isGetLock) {
                        break;
                    }else {
                        //没抢到锁，先自旋
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                LOGGER.info("我是线程{}-start", Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("我是线程{}-end", Thread.currentThread().getName());
                //释放
                redisLockUtil.releaseLock(lockKey, requestId);
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }


    /**
     * RedisDistributedLockHandler使用例子
     * http://localhost:8080/kunsharedemo/redisClusterLock-demo/test2
     * @return
     */
    @GetMapping("/test2")
    public ResultVo<String> test2(){
        String lockKey = "redisClusterLock-lockkey";
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                //上锁
                redisClusterLockHandler.lock(lockKey);
                LOGGER.info("我是线程{}-start", Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("我是线程{}-end", Thread.currentThread().getName());
                //解锁
                redisClusterLockHandler.unlock(lockKey);
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

    /**
     * RedisDistributedLockHandler使用例子
     * http://localhost:8080/kunsharedemo/redisClusterLock-demo/test2
     * @return
     */
    @GetMapping("/test2BySleep60Seconds")
    public ResultVo<String> test2BySleep60Seconds(){
        String lockKey = "redisClusterLock-lockkey";
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                //上锁
                redisClusterLockHandler.lock(lockKey);
                LOGGER.info("我是线程{}-start", Thread.currentThread().getName());
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("我是线程{}-end", Thread.currentThread().getName());
                //解锁
                redisClusterLockHandler.unlock(lockKey);
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

    @GetMapping("/test2By2ThreadSleep60Seconds")
    public ResultVo<String> test2By2ThreadSleep60Seconds() throws InterruptedException {
        String lockKey = "redisClusterLock-lockkey-2";
        new Thread(()->{
            //上锁
            redisClusterLockHandler.lock(lockKey);
            LOGGER.info("我是线程{}-start", Thread.currentThread().getName());
            try {
                Thread.sleep(1800000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("我是线程{}-end", Thread.currentThread().getName());
            //解锁
            redisClusterLockHandler.unlock(lockKey);
        }).start();

        Thread.sleep(1000);

        new Thread(()->{
            //上锁
            redisClusterLockHandler.lock(lockKey);
            LOGGER.info("我是线程{}-start", Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("我是线程{}-end", Thread.currentThread().getName());
            //解锁
            redisClusterLockHandler.unlock(lockKey);
        }).start();


        return ResultVo.valueOfSuccess("");
    }

}
