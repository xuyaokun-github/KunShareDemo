package cn.com.kun.foo.javacommon.completableFuture;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * demo-子线程抛异常-父线程阻塞问题
 *
 * author:xuyaokun_kzx
 * date:2023/3/31
 * desc:
*/
public class TestCompletableFuture3 {

    public static void main(String[] args) {

        try {
            // 创建异步执行任务:
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
            ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "TestCompletableFuture3-Pool" + UUID.randomUUID().toString());
                }
            });

            Future<Double> cf = executorService.submit(()->{
                System.out.println(Thread.currentThread()+" start,time->"+System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                boolean flag = true;
//            flag = false;
                if(flag){
                    throw new RuntimeException("test");
                }else{
                    System.out.println(Thread.currentThread()+" exit,time->"+System.currentTimeMillis());
                    return 1.2;
                }
            });
            System.out.println("main thread start,time->"+System.currentTimeMillis());
            //等待子任务执行完成,如果已完成则直接返回结果
            //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
            //主线程会被阻塞
            System.out.println("run result->"+cf.get());
            System.out.println("main thread exit,time->"+System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("The end!");
    }


}
