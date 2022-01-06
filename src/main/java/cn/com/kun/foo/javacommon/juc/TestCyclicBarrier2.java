package cn.com.kun.foo.javacommon.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestCyclicBarrier2 {

    public static AtomicBoolean isOver = new AtomicBoolean(false);
    public static AtomicBoolean isFinishOne = new AtomicBoolean(false);

    public static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {

        //
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("人到齐了，开始扣篮大赛....");
                isFinishOne.set(true);
            }
        });
        //一开始时，getParties输出就是4
        System.out.println(cyclicBarrier.getParties());

        new Thread(new PlayerThread(cyclicBarrier, "Tracy McGrady", isOver ,count)).start();
        new Thread(new PlayerThread(cyclicBarrier, "James Harden", isOver,count)).start();
        new Thread(new PlayerThread(cyclicBarrier, "Yao Ming", isOver,count)).start();
        new Thread(new PlayerThread(cyclicBarrier, "Vince Carter", isOver,count)).start();



        //开一个线程，用来重置屏障
//        new Thread(()->{
//
//            for (;;){
//                if (isFinishOne.get()){
//                    try {
//                        //休息三秒钟，开始下一轮
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    //调用reset之后，cyclicBarrier会重新变为4
//                    System.out.println("一轮结束，即将开始下一轮");
//                    //其实不需要重置，它会自动重置
////                    cyclicBarrier.reset();
//                    System.out.println("重置之后，cyclicBarrier:" + cyclicBarrier.getParties());
//                    //所有线程必须集齐四个await方法（调用四次await之后，所有线程会一起执行）
//                    //把标志改成false，让球员知道，拉拉队已退场，可以开始进场准备了
//                    isFinishOne.set(false);
//                }
//            }
//        }).start();


    }


    static class PlayerThread implements Runnable{

        private CyclicBarrier cyclicBarrier;
        private String name;
        private AtomicBoolean isOver;
        private AtomicInteger count;

        public PlayerThread(CyclicBarrier cyclicBarrier, String name, AtomicBoolean isOver,
                            AtomicInteger count) {
            this.cyclicBarrier = cyclicBarrier;
            this.name = name;
            this.isOver = isOver;
            this.count = count;
        }

        @Override
        public void run() {

            for (;;){

//                if (isOver.get()){
//                    System.out.println(this.name + "离开球馆！");
//                    break;
//                }
//
//                if (isFinishOne.get()){
//                    continue;
//                }
                //球员开始准备
                System.out.println(this.name + " is ready.....");

                try {
                    this.cyclicBarrier.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                try {
                    System.out.println(this.name + " dunking.....");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        }


    }
}
