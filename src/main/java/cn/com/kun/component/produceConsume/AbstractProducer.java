package cn.com.kun.component.produceConsume;

/**
 * 这是一个生产者，它就是一个线程，
 * 不断运行产生东西（任务），让消费者去消费
 * @author Kunghsu
 * @datetime 2018年6月2日 下午10:45:36
 * @desc
 */
public abstract class AbstractProducer implements Producer, Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
