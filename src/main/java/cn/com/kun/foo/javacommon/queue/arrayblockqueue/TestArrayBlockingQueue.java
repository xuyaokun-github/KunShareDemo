package cn.com.kun.foo.javacommon.queue.arrayblockqueue;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestArrayBlockingQueue {

    static BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(200000);

    public static void main(String[] args) throws InterruptedException {

        boolean isParallel = true;
//        boolean isParallel = false;

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " 开始生产");
                    for (int j = 0; j < 1000; j++) {
                        try {
                            blockingQueue.put(Thread.currentThread().getName() + "-" + j);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }).start();
        }

        Thread.sleep(3000);

        System.out.println("目前队列总数：" + blockingQueue.size());

        new Thread(() -> {

            long start = System.currentTimeMillis();
            while (true) {
                List<String> sourceList = new ArrayList<>();
                blockingQueue.drainTo(sourceList, 20);

                if (CollectionUtils.isEmpty(sourceList)) {
                    System.out.println("总耗时：" + (System.currentTimeMillis() - start));
                    break;
                }

                if (isParallel) {
                    sourceList.stream().parallel().forEach(s -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(s);
                    });
                } else {
                    sourceList.forEach(s -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(s);
                    });
                }


            }
        }).start();

    }
}
