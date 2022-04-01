package cn.com.kun.component.intervalCache.demo2;

import cn.com.kun.component.intervalCache.demo1.IntervalCacheDataLoader;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestIntervalCache2 {

    public static void main(String[] args) {

        IntervalCacheDataLoader dataLoader = new IntervalCacheDataLoader() {
            @Override
            public <T> T loadData() {
                String time = UUID.randomUUID().toString();
                return (T) time;
            }
        };

        IntervalCache2 intervalCache2 = new IntervalCache2(TimeUnit.SECONDS, 3L, dataLoader);
//        IntervalCache2 intervalCache2 = new IntervalCache2(TimeUnit.MILLISECONDS, 10L, dataLoader);

        for (int i = 0; i < 1; i++) {
            //刷新的线程，自己控制尽量只保留一个
            new Thread(()->{
                while (true){
                    intervalCache2.check();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"update-thread-" + i).start();

        }

        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                while (true){
                    System.out.println(Thread.currentThread().getName() + "获取到值：" + intervalCache2.get());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "get-thread-" + i).start();
        }

    }

}
