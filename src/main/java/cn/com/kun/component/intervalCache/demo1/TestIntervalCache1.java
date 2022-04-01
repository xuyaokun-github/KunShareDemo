package cn.com.kun.component.intervalCache.demo1;

import java.util.UUID;

public class TestIntervalCache1 {

    public static void main(String[] args) {

        IntervalCacheDataLoader dataLoader = new IntervalCacheDataLoader() {
            @Override
            public <T> T loadData() {
                String time = UUID.randomUUID().toString();
                return (T) time;
            }
        };

        IntervalCache1 intervalCache1 = new IntervalCache1(1000 * 10L, dataLoader);

        for (int i = 0; i < 1; i++) {
            //刷新的线程，自己控制尽量只保留一个
            new Thread(()->{
                while (true){
                    intervalCache1.check();
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
                    System.out.println(Thread.currentThread().getName() + "获取到值：" + intervalCache1.get());
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
