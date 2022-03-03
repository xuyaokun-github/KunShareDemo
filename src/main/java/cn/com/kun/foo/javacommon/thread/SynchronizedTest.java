package cn.com.kun.foo.javacommon.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 当 synchronized 遇到Integer，有个大坑
 *
 * author:xuyaokun_kzx
 * date:2022/3/1
 * desc:
*/
public class SynchronizedTest {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        Thread why = new Thread(new TicketConsumer(atomicInteger), "why");
        Thread mx = new Thread(new TicketConsumer(atomicInteger), "mx");
        why.start();
        mx.start();
    }

}

class TicketConsumer implements Runnable {
//    private volatile static Integer ticket;
    private volatile static AtomicInteger ticket;

    public TicketConsumer(AtomicInteger ticket) {
        this.ticket = ticket;
    }

    @Override
    public void run() {
        while (true) {
//            System.out.println(Thread.currentThread().getName() + "开始抢第" + ticket + "张票，对象加锁之前：" + System.identityHashCode(ticket));
            synchronized (ticket) {
//                System.out.println(Thread.currentThread().getName() + "抢到第" + ticket + "张票，成功锁到的对象：" + System.identityHashCode(ticket));
                if (ticket.get() > 0) {
                    try {
                        //模拟抢票延迟
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "抢到了第" + ticket.getAndDecrement() + "张票，票数减一");
                } else {
                    return;
                }
            }
        }
    }
}
