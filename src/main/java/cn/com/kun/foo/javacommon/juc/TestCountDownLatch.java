package cn.com.kun.foo.javacommon.juc;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {

        TestCountDownLatch testCondition = new TestCountDownLatch();

        /*
            子线程
         */
        new Thread(()->{
            testCondition.doB();
        }, "B").start();

        /*
         * 父线程
         */
        new Thread(()->{
            testCondition.doA();
        }, "A").start();



        ThreadUtils.runForever();
    }

    private void doA() {
        while (true){
            try {
                String now = DateUtils.now();
                System.out.println("执行A逻辑" + " " + now);
                Thread.sleep(1000);
                if (now.endsWith("0")){
                    System.out.println("A逻辑发送通知");
                    countDownLatch.countDown();
                    //不需要调reset()，它会自动重置
//                    cyclicBarrier.reset();
//                    Thread.sleep(5000);
                    countDownLatch = new CountDownLatch(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
            }
        }
    }

    private void doB() {
        while (true){
            try {
                System.out.println("------------------------执行B逻辑" + " " + DateUtils.now());
//                Thread.sleep(3000);
                countDownLatch.await();
                System.out.println("B逻辑阻塞结束");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
            }
        }
    }




}
