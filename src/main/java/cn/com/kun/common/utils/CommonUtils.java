package cn.com.kun.common.utils;

/**
 * author:xuyaokun_kzx
 * date:2023/1/12
 * desc:
*/
public class CommonUtils {

    public static final String EMPTY = "";

    public static String valueOf(Object obj) {

        return obj == null? EMPTY : String.valueOf(obj);
    }

}
