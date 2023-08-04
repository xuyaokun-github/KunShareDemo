package cn.com.kun.foo.javacommon.exception;

/**
 * jvm的异常堆栈优化
 *
 * author:xuyaokun_kzx
 * date:2023/8/4
 * desc:
*/
public class StackTraceInFastThrowDemo {

    public static void main(String[] args) {
        int count = 0;
        boolean flag = true;
        while (flag) {
            try {
                count++;
                npeTest(null);
            } catch (Exception e) {
                int stackTraceLength = e.getStackTrace().length;
                System.out.printf("count: %d, stacktrace length: %d%n", count, stackTraceLength);
                if (stackTraceLength == 0) {
                    flag = false;
                }
            }
        }
    }

    public static void npeTest(Integer i) {
        System.out.println(i.toString());
    }
}
