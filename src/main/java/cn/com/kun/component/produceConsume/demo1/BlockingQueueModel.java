package cn.com.kun.component.produceConsume.demo1;

import cn.com.kun.component.produceConsume.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 第一种方式 实现消费者生产者模型
 * @author Kunghsu
 * @datetime 2018年6月2日 下午10:46:50
 * @desc
 */
public class BlockingQueueModel implements Model {

    private final BlockingQueue<Task> queue;
    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    public BlockingQueueModel(int cap) {
        // LinkedBlockingQueue 的队列是 lazy-init 的，但 ArrayBlockingQueue 在创建时就已经 init
        this.queue = new LinkedBlockingQueue<>(cap);
    }

    /*
     * 下面这两个是工厂方法，用来new 生产者和消费者
     */
    @Override
    public Runnable newRunnableConsumer() {
        return new ConsumerImpl();
    }
    @Override
    public Runnable newRunnableProducer() {
        return new ProducerImpl();
    }

    private class ConsumerImpl extends AbstractConsumer implements Consumer, Runnable {
        @Override
        public void consume() throws InterruptedException {
            //进行消费，从队列中取内容
            Task task = queue.take();
            //模拟一个耗时，固定时间范围的消费，模拟相对稳定的服务器处理过程
            Thread.sleep(500 + (long) (Math.random() * 500));
            //输出消费过程日志
            System.out.println("consume: " + task.no);
        }
    }

    private class ProducerImpl extends AbstractProducer implements Producer, Runnable {

        @Override
        public void produce() throws InterruptedException {
            //模拟 不定期生产，模拟随机的用户请求
            Thread.sleep((long) (Math.random() * 1000));
            //产生新任务
            Task task = new Task(increTaskNo.getAndIncrement());
            //放入队列
            queue.put(task);
            System.out.println("produce: " + task.no);
        }
    }

    /**
     * 测试代码
     * @param args
     */
    public static void main(String[] args) {
        // 建一个队列长度为3的LinkedBlockingQueue
        Model model = new BlockingQueueModel(3);
        //启动2个消费者进程
        for (int i = 0; i < 2; i++) {
            new Thread(model.newRunnableConsumer()).start();
        }
        //启动5个生产者进程
        for (int i = 0; i < 5; i++) {
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
