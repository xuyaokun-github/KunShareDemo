package cn.com.kun.foo.javacommon.queue.arrayblockqueue.produceAndConsume;

import cn.com.kun.common.utils.DateUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProduceAndConsumeDemo1 {

    static BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(200000);

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Start: " + DateUtils.now());

        boolean isParallel = true;
//        boolean isParallel = false;

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " 开始生产");
                    for (int j = 0; j < 2000; j++) {
                        try {
                            blockingQueue.put(Thread.currentThread().getName() + "-" + j);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("入队结束 " + DateUtils.now());
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

//        Thread.sleep(3000);

//        System.out.println("目前队列总数：" + blockingQueue.size());

        //多线程入队，单线程出队
//        new Thread(() -> {
//
//            long start = System.currentTimeMillis();
//            while (true) {
//                List<String> sourceList = new ArrayList<>();
//                blockingQueue.drainTo(sourceList, 20);
//
//                if (CollectionUtils.isEmpty(sourceList)) {
//                    System.out.println("总耗时：" + (System.currentTimeMillis() - start));
//                    break;
//                }
//
//                if (isParallel) {
//                    sourceList.stream().parallel().forEach(s -> {
//                        doService(s);
//                    });
//                } else {
//                    sourceList.forEach(s -> {
//                        doService(s);
//                    });
//                }
//            }
//        }).start();

    }

    private static void doService(String s) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(s + " " + DateUtils.now());
    }


}
