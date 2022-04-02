package cn.com.kun.component.produceConsume;

/**
 * 这是一个抽象的消费者，它负责就是执行接口定义的消费方法
 * 当然这是一个抽象类，是提供出来被人实现的
 * @author Kunghsu
 * @datetime 2018年6月2日 下午10:44:48
 * @desc
 */
public abstract class AbstractConsumer implements Consumer, Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
