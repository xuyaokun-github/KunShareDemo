package cn.com.kun.foo.javacommon.juc.reentrantReadWriteLock;


import cn.com.kun.common.vo.ResultVo;

public class TestLock2 {

    public static void main(String[] args) {


        //启动三个du读线程
        for (int i = 0; i < 3; i++) {

            int finalI = i;
            new Thread(()->{

                for (;;){
                    String value = ReadWriteLockDemo1.get("" + finalI);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }, "read-thread-" + i).start();

        }

        //启动两个更新线程
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (;;){
                    ResultVo retResult = ReadWriteLockDemo1.reload();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "update-thread-" + i).start();
        }


    }
}
