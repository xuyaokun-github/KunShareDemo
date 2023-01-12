package cn.com.kun.foo.javacommon.string;

import cn.com.kun.common.utils.CommonUtils;

public class TestStringValueOf {

    public static void main(String[] args) {

        Object obj = null;
        //输出"null"
        System.out.println(String.valueOf(obj));
        System.out.println(CommonUtils.valueOf(obj));

        /*
            报空指针异常
            因为valueOf有多个重载方法，这个进的是 java.lang.String.valueOf(char[])
         */
        System.out.println(CommonUtils.valueOf(null));

    }

    public static String trimToEmpty(Object obj){

        return obj == null? "" : String.valueOf(obj);
    }

}
