package cn.com.kun.foo.javacommon.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestExecutors {

    /**
     *
     * 虽然是静态的，只有一个对象，但是它可以同时执行多种类型的任务
     * 所以线程池的数目在运行多个任务的情况下有用
     */
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    public static void main(String[] args) {

        scheduledExecutorService.scheduleAtFixedRate(()->{

            try {
                System.out.println("one running!!!!!" + Thread.currentThread().getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 0L, 10L, TimeUnit.SECONDS);

        scheduledExecutorService.scheduleAtFixedRate(()->{

            try {
                System.out.println("two running!!!!!" + Thread.currentThread().getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 0L, 10L, TimeUnit.SECONDS);

        scheduledExecutorService.scheduleAtFixedRate(()->{

            try {
                System.out.println("three running!!!!!" + Thread.currentThread().getName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 0L, 10L, TimeUnit.SECONDS);
    }


}
