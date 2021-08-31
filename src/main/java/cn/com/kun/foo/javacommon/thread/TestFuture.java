package cn.com.kun.foo.javacommon.thread;

import cn.com.kun.common.vo.ResultVo;

import java.util.concurrent.*;

public class TestFuture {

    static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Future future;
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(5000);
                return ResultVo.valueOfSuccess();
            }
        };
        future = threadPoolExecutor.submit(callable);
        System.out.println(future.isDone());
        Object obj = future.get();
        Object obj1 = future.get();
        //get方法可以调多次
        System.out.println(String.format("obj == obj1: %s", obj == obj1));
        System.out.println(future.isDone());
        while (true){
            Thread.sleep(1000);
        }
    }
}
