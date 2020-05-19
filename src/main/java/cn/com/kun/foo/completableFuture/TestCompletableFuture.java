package cn.com.kun.foo.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestCompletableFuture {

    public static void main(String[] args) {

        //
        CompletableFuture.runAsync(() -> {}).thenRun(() -> {});
        CompletableFuture.runAsync(() -> {}).thenAccept(resultA -> {});
        CompletableFuture.runAsync(() -> {}).thenApply(resultA -> "resultB");

        CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {});
        CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {});
        CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB");


        //无结果地按顺序执行
        /**
         * 假如还有多个，可以继续用thenRun链接起来
         * thenRun方法返回的是一个CompletableFuture实例
         */
        CompletableFuture.runAsync(() -> {
            System.out.println("我是A任务");
        }).thenRun(() -> {
            System.out.println("我是B任务");
        });

        //有返回值地按顺序执行
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行A任务");
            return "ResultA";
        }).thenApply(resultA -> {
            System.out.println("B任务中收到的A任务的结果:" + resultA);
            return "ResultB";
        });
        try {
            //输出B任务的结果
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        /**
         * thenAccept和thenApply的区别在于，thenApply必须有返回值，thenAccept不需要返回值
         */
        CompletableFuture future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行A任务");
            return "ResultA";
        }).thenAccept(resultA -> {
            System.out.println("B任务中收到的A任务的结果:" + resultA);
            //假如这里写了return就会报错
        });


        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> {
            System.out.println("i am job a.");
            return "resultA";
        });
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> {
            System.out.println("i am job b.");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "resultB";
        });

        cfA.thenCombine(cfB, (resultA, resultB) -> {
            //合并结果的处理
            System.out.println("合并后内容：" + resultA + resultB);
            //thenCombine和thenAcceptBoth的区别在于：thenCombine在处理完之后必须要有返回值
            return resultA + resultB;
        });

        //因为上面的cfA和cfB是并行的任务，同等的，所以用cfB调用thenCombine也是等价的
        //并且合并结果这个动作可以进行多次！
        cfB.thenCombine(cfA, (resultB, resultA) -> {
            //合并结果的处理
            System.out.println("合并后内容：" + resultA + resultB);
            //thenCombine和thenAcceptBoth的区别在于：thenCombine在处理完之后必须要有返回值
            return resultA + resultB;
        });

//        cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {});
//        cfA.runAfterBoth(cfB, () -> {});

        System.out.println("-------------");


        CompletableFuture<CompletableFuture<String>> future3 = CompletableFuture
                .supplyAsync(() -> "hello")
                .thenApply(cfA3 -> CompletableFuture.supplyAsync(() -> cfA3 + " world"));

        try {
            String str = future3.get().get();
            System.out.println(str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        while (true){}
    }
}
