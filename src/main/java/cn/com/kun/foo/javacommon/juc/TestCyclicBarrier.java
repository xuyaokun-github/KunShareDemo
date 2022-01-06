package cn.com.kun.foo.javacommon.juc;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;

import java.util.concurrent.CyclicBarrier;

/**
 * 父线程控制子线程的启停
 *
 * author:xuyaokun_kzx
 * date:2022/1/5
 * desc:
*/
public class TestCyclicBarrier {

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {

        TestCyclicBarrier testCondition = new TestCyclicBarrier();

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
                    /*
                        这里有一个风险点，假如子线程处理逻辑很重，迟迟不执行甚至一直不执行await()方法
                        这里会一直阻塞，父线程相当于卡死
                        用CountDownLatch就不会有这个风险，因为CountDownLatch只需要减一然后重新new CountDownLatch即可
                     */

                    //这里用一个带超时的阻塞,也是有问题的，因为超时后计数器会被重置，子线程那边await会抛异常
//                    cyclicBarrier.await(2, TimeUnit.SECONDS);
                    cyclicBarrier.await();

                    //不需要调reset()，它会自动重置
//                    cyclicBarrier.reset();
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
                cyclicBarrier.await();
                Thread.sleep(30000);
                System.out.println("B逻辑阻塞结束");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
            }
        }
    }




}
