package cn.com.kun.foo.javacommon.juc;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 *
 * author:xuyaokun_kzx
 * date:2021/9/17
 * desc:
*/
public class ExchangerTest {

    public static List<String> PRODUCE_BUFFER = null;

    public static List<String> CONSUMER_BUFFER = null;


    static class Producer implements Runnable{

        //生产者、消费者交换的数据结构
        private List<String> buffer;
        //步生产者和消费者的交换对象
        private Exchanger<List<String>> exchanger;

        Producer(List<String> buffer, Exchanger<List<String>> exchanger){
            this.buffer = buffer;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for(int i = 1 ; i < 5; i++){
                System.out.println("生产者第" + i + "次提供");
                for(int j = 1 ; j <= 3 ; j++){
                    System.out.println("生产者装入" + i  + "--" + j);
                    buffer.add("buffer：" + i + "--" + j);
                }
                //生产者放入三个数据
                System.out.println("生产者装完三个元素，等待与消费者交换,开始调exchange方法...");
                System.out.println("生产者调exchange前，buffer:" + JacksonUtils.toJSONString(buffer) + "buffer的引用：" + buffer.hashCode());
                try {
                    //假如不调这个方法，会怎样？效果就是生产者一直在放自己的buffer，没意义
                    //假如消费者一直不调用exchange方法，会怎样？ 生产者这里会阻塞住，因为上面add进去的数据，它根本没拿。所以生产者要停一下
                    //假如消费者那边调用了exchange，那消费者就会拿到当前这个buffer
                    exchanger.exchange(buffer);
                    //因为这里是生产者模式，生产者不关心消费者返回的数据，所以可以不接
                    System.out.println("生产者exchange方法执行完毕...生产者调exchange后，buffer:" + JacksonUtils.toJSONString(buffer) + "buffer的引用：" + buffer.hashCode());

                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private List<String> buffer;
        private final Exchanger<List<String>> exchanger;

        public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
            this.buffer = buffer;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 1; i < 5; i++) {
                System.out.println("消费者第" + i + "次提取");
                //调用exchange()与消费者进行数据交换
                try {
                    //假如生产那边一直不调用exchange，消费者这边会发现不了新数据，会阻塞在这一行
                    System.out.println("消费者调exchange前，buffer:" + JacksonUtils.toJSONString(buffer));
                    //消费者这里必须要用新拿到的引用进行操作
                    //这里拿到的引用实质上，就是生产者那边传过来的对象
                    buffer = exchanger.exchange(buffer);
                    System.out.println("消费者调exchange后，buffer:" + JacksonUtils.toJSONString(buffer) + "buffer的引用：" + buffer.hashCode()
                            + " 消费者里比较指针是否等于生产者的引用：" + (buffer == PRODUCE_BUFFER));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (buffer.size() == 0){
                    return;
                }

                /*
                    假如这里消费得很慢，会出现什么现象？
                    假如生产者放了1-3进队列，此时调用了exchange，消费者此时也刚好调用了exchange，达成共识
                    此时消费者会拿到这个1-3，准备开始处理的时候，因为处理得很慢，此时生产者还在不断生产，因为引用是同一个
                    所以消费者这里会看到数据内容出现幻读！数据突然变多了，可以用耗时模拟一下这个场景

                    但就算这样也没关系，只要同一个引用妥善处理，也不会有线程安全问题
                    因为只有简单的操作，一个是get，一个是add.
                 */

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("睡眠后的buffer:" + JacksonUtils.toJSONString(buffer));

                System.out.println("消费者开始循环获取buffer数据");
                for (int j = 1; j <= 3 ; j++) {
                    System.out.println("消费者准备调get方法：");
                    System.out.println("消费者 : " + buffer.get(0));
                    //假如消费者不remove，会有什么问题？
                    //那消费者拿到的就始终同一个元素，无法向后取元素
                    buffer.remove(0);
                    //
                }
            }
        }
    }

    public static void main(String[] args){

        List<String> buffer1 = new ArrayList<String>();
        List<String> buffer2 = new ArrayList<String>();
        Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
        PRODUCE_BUFFER = buffer1;
        Thread producerThread = new Thread(new Producer(buffer1, exchanger));
        CONSUMER_BUFFER = buffer2;
        Thread consumerThread = new Thread(new Consumer(buffer2, exchanger));
        producerThread.start();
        consumerThread.start();
    }

}
