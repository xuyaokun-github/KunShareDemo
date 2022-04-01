package cn.com.kun.foo.javacommon.juc.atomicReference;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 如果有一家蛋糕店，为了挽留客户，绝对为贵宾卡里余额小于20元的客户一次性赠送20元，刺激消费者充值和消费。但条件是，每一位客户只能被赠送一次。
 *
 * 用AtomicStampedReference解决ABA问题
 * 亲测确实避免了ABA问题，避免同一个赋值成功了多次
 * 但这种场景，更适合的不是AtomicStampedReference，也可以用悲观锁来解决。AtomicStampedReference这是一种乐观锁的思想
 *
 * author:xuyaokun_kzx
 * date:2022/4/1
 * desc:
*/
public class AtomicReferenceTest2 {

    // 创建AtomicStampedReference对象，持有Foo对象的引用，初始为null，版本为0
    // 设置账户初始值小于20，显然这是一个需要被充值的账户
    static AtomicStampedReference<Integer>  money = new AtomicStampedReference<>(19,0);

    public static void main(String[] args) {

        //模拟多个线程同时更新后台数据库，为用户充值
        for (int i = 0; i < 3; i++) {
            new Thread() {
                public void run() {
                    //这个while (true) 是为了模拟一直扫描数据库中 需要充值客户的数据 防止下面的条件break结束循环扫描过程
                    while (true) {
                        while (true) {
//                            System.out.println("充值线程一直在扫描--需要充值的客户数据");
                            Integer m = money.getReference();
                            if (m < 20) {
                                //这里必须要知道初始版本，才能避免ABA问题，这里的初始版本就是0
                                if (money.compareAndSet(m, m + 20, 0,money.getStamp()+1 )) {
                                    System.out.println("余额小于20元，充值成功，余额:" + money.getReference() + "元");
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
                        Integer m = money.getReference();
                        if (m > 10) {
                            System.out.println("----------------大于10元");
                            //注意，在消费这里，不需要关注ABA问题，只需要做原子减即可。只有充值时才需要关注ABA问题
                            if (money.compareAndSet(m, m - 10, money.getStamp(),money.getStamp()+1)) {
                                System.out.println("----------------成功消费10元，余额:" + money.getReference());
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
