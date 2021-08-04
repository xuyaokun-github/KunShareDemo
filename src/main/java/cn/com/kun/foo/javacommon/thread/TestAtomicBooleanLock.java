package cn.com.kun.foo.javacommon.thread;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用AtomicBoolean实现自旋锁
 *
 * author:xuyaokun_kzx
 * date:2021/8/2
 * desc:
*/
public class TestAtomicBooleanLock {

    private AtomicBoolean isLock = new AtomicBoolean(false);

    public void doWork(){

        //尝试抢锁
        while (!isLock.compareAndSet(false, true)){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //抢锁成功

        //执行具体业务逻辑
        System.out.println(Thread.currentThread().getName() + "开始执行具体业务");
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "结束执行具体业务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //释放锁
            isLock.set(false);
        }

    }

    public static void main(String[] args) {

        TestAtomicBooleanLock testAtomicBooleanLock = new TestAtomicBooleanLock();

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                testAtomicBooleanLock.doWork();
            }, "thread-" + i).start();
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
