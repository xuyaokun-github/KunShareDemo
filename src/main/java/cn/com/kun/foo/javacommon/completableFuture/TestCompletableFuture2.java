package cn.com.kun.foo.javacommon.completableFuture;

import cn.com.kun.common.utils.DateUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCompletableFuture2 {

    public static void main(String[] args) {

        thenApply();

    }

    public static void thenApply() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture cf = CompletableFuture.supplyAsync(() -> {
            //这是一个异步任务，放入executorService线程池执行
            try {
                  Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("supplyAsync " + Thread.currentThread().getName() + " " + DateUtils.now());
            return "hello";
        }, executorService) //放入线程池之后就拿到一个CompletableFuture对象
                .thenApplyAsync(s -> {
                    //然后又放入一个异步任务.
                    //这个异步任务肯定会是上一个异步任务完成之后执行
                    System.out.println(s + "world"  + " " + DateUtils.now());
                    return "hhh";
                }, executorService);

        cf.thenRunAsync(() -> {
            //异步化
            System.out.println("ddddd " + Thread.currentThread().getName() + " " + DateUtils.now());
        });
        cf.thenRun(() -> {
            System.out.println("ddddsd " + Thread.currentThread().getName() + " " + DateUtils.now());
        });
        cf.thenRun(() -> {
            System.out.println("dddaewdd " + Thread.currentThread().getName() + " " + DateUtils.now());
        });
    }
}
