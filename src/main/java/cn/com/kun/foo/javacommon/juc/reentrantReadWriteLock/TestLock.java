package cn.com.kun.foo.javacommon.juc.reentrantReadWriteLock;


import cn.com.kun.common.vo.ResultVo;

public class TestLock {

    public static void main(String[] args) {


        //启动三个du读线程
        for (int i = 0; i < 3; i++) {

            int finalI = i;
            new Thread(()->{

                for (;;){
                    System.out.println(Thread.currentThread().getName() + "：准备开始get");
                    String value = SysConfigProvider.get("" + finalI);
                    System.out.println(Thread.currentThread().getName() + "：获取到值：" + value);
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
                    System.out.println(Thread.currentThread().getName() + "：准备开始更新缓存数据");
                    ResultVo retResult = SysConfigProvider.reload();
                    System.out.println(Thread.currentThread().getName() + "：更新缓存数据完毕！！！！！ " + retResult.getValue());

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
