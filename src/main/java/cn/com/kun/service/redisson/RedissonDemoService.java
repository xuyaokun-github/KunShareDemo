package cn.com.kun.service.redisson;

import cn.com.kun.common.config.redisson.RedissonAutowired;
import cn.com.kun.utils.DateUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

@Component
public class RedissonDemoService {

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

}
