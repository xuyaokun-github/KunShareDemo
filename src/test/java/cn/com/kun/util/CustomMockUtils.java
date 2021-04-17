package cn.com.kun.util;

import cn.com.kun.common.utils.DateUtils;

public class CustomMockUtils {


    public static void sleepRun(){

        System.out.println("start:" + DateUtils.now());
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(methodName + " current thread:" + Thread.currentThread().getName());
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("end:" + DateUtils.now());
    }
}
