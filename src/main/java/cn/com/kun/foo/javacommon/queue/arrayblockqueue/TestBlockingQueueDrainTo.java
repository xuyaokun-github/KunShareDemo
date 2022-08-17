package cn.com.kun.foo.javacommon.queue.arrayblockqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * drainTo方法的反例demo
 *
 * author:xuyaokun_kzx
 * date:2022/8/11
 * desc:
*/
public class TestBlockingQueueDrainTo {

    private static BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(200000);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            blockingQueue.put(UUID.randomUUID().toString());
        }

        //模拟一个消费者进行持续消费
        //这里稍不注意，会有内存泄漏(正确的做法，应该把list放到while循环内.否则会出现内存泄漏)
//        List<String> resList = new ArrayList<>();//反例
        List<String> resList = null;
        while (true){
            resList = new ArrayList<>();//正例
            int count = blockingQueue.drainTo(resList, 100);
            System.out.println(count);
            if (count <= 0){
                break;
            }
        }

        System.out.println(resList.size());
    }


}
