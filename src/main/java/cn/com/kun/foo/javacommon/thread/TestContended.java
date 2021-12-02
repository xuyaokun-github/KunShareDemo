package cn.com.kun.foo.javacommon.thread;

import sun.misc.Contended;

import java.util.stream.IntStream;

/**
 * 伪共享的实验例子
 *
 * author:xuyaokun_kzx
 * date:2021/11/30
 * desc:
*/
public class TestContended {

    static class Data {

        //必须开启 -XX:-RestrictContended，注解才生效
        @Contended
        volatile long a;
        volatile long b;
    }

    public static void main(String[] args) throws InterruptedException {

        Data data = new Data();
        Thread thread1 = new Thread(()->{
            IntStream.range(0, 50000000).forEach((i) -> data.a++);
        });
        Thread thread2 = new Thread(()->{
            IntStream.range(0, 50000000).forEach((i) -> data.b++);
        });

        long start = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(System.currentTimeMillis() - start);
    }

}
