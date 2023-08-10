package cn.com.kun.foo.javacommon.thread;

/**
 * 源码分析
 * author:xuyaokun_kzx
 * desc:
*/
public class TestThreadLocal {

    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {

        Thread.currentThread().interrupt();
        Thread.currentThread().isInterrupted();
        Thread.interrupted();

        new TestThreadLocal().run();
        System.out.println();
    }

    public void run() {
        threadLocal.get();
    }


}
