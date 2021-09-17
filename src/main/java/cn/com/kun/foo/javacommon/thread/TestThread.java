package cn.com.kun.foo.javacommon.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 按顺序输出ABC，Java使用三个线程循环顺序打印A、B、C
 * 假如需要限制次数，搞一个变量计数即可
 * Created by xuyaokun
 * @desc:
 */
public class TestThread {

    public static ReentrantLock printLock = new ReentrantLock();
    static Condition aCondition = printLock.newCondition();
    static Condition bCondition = printLock.newCondition();
    static Condition cCondition = printLock.newCondition();

    public static void main(String[] args) throws Exception {
        new Thread(()->{
            while(true){
                printLock.lock();
                bCondition.signalAll();
                System.out.println("A");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    aCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printLock.unlock();
            }
        }, "A").start();
        new Thread(()->{
            while(true){
                printLock.lock();
                cCondition.signalAll();
                System.out.println("B");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    bCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printLock.unlock();

            }
        }, "B").start();
        new Thread(()->{
            while(true){
                printLock.lock();
                aCondition.signalAll();
                System.out.println("C");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    cCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printLock.unlock();

            }
        }, "C").start();
    }
}
