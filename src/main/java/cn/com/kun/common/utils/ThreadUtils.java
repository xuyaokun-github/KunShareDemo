package cn.com.kun.common.utils;

/**
 * 线程工具类
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
*/
public class ThreadUtils {

    public static void main(String[] args) {
        System.out.println(getCurrentInvokeClassAndMethod());
    }

    /**
     * 获取当前正在执行的函数的全限定名及方法
     * 例如：cn.com.kun.common.utils.ThreadUtils#getCurrentInvokeClassAndMethod
     * @return
     */
    public static String getCurrentInvokeClassAndMethod(){
        //1表示当前方法，2表示外部调用了当前方法的方法
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        return stackTraceElement.getClassName() + "#" + stackTraceElement.getMethodName();
    }

    /**
     * 永久运行
     */
    public static void runForever(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logWithThreadInfo(String msg){
        System.out.println(String.format("%s ----[当前线程：%s]", msg, Thread.currentThread().getName()));
    }

}
