package cn.com.kun.foo.javacommon.juc.atomicReference;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 如果有一家蛋糕店，为了挽留客户，绝对为贵宾卡里余额小于20元的客户一次性赠送20元，刺激消费者充值和消费。但条件是，每一位客户只能被赠送一次。
 *
 * author:xuyaokun_kzx
 * date:2022/4/1
 * desc:
*/
public class AtomicReferenceTest {

    static AtomicReference<Integer> money = new AtomicReference<Integer>();


    public static void main(String[] args) {
        // 设置账户初始值小于20，显然这是一个需要被充值的账户
        money.set(19);

        //模拟多个线程同时更新后台数据库，为用户充值
        for (int i = 0; i < 3; i++) {
            new Thread() {
                public void run() {
                    //这个while (true) 是为了模拟一直扫描数据库中 需要充值客户的数据 防止下面的条件break结束循环扫描过程
                    while (true) {
                        while (true) {
//                            System.out.println("充值线程一直在扫描--需要充值的客户数据");
                            Integer m = money.get();
                            if (m < 20) {
                                if (money.compareAndSet(m, m + 20)) {
                                    System.out.println("余额小于20元，充值成功，余额:" + money.get() + "元");
                                    break;
                                }
                            } else {
                                //System.out.println("余额大于20元，无需充值");
                                break;
                            }
                        }
                    }
                }
            }.start();
        }


        //用户消费线程，模拟消费行为
        new Thread() {
            public void run() {
                for (int i = 0; i < 20; i++) {
                    while (true) {
                        Integer m = money.get();
                        if (m > 10) {
                            System.out.println("----------------大于10元");
                            if (money.compareAndSet(m, m - 10)) {
                                System.out.println("----------------成功消费10元，余额:" + money.get());
                                break;
                            }
                        } else {
                            System.out.println("----------------没有足够的金额");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }.start();
    }

}
