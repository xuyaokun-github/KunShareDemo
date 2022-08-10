package cn.com.kun.foo.javacommon.string;

import cn.com.kun.common.utils.DateUtils;

import java.util.Date;

public class TestStringCompare2 {


    public static void main(String[] args) {

        Date date = DateUtils.toDate("2022-08-10 14:34:20.41", "yyyy-MM-dd HH:mm:ss.SSS");


        System.out.println("输出-1表示小于，输出1表示大于");
        System.out.println("2022-08-10 14:34:20.471".compareTo("2022-08-10 14:34:20.481"));
        System.out.println("2022-08-10 14:34:20.491".compareTo("2022-08-10 14:34:20.481"));

        System.out.println("2022-08-10 14:34:20.000".compareTo("2022-08-10 14:34:20.481"));
        System.out.println("2022-08-10 14:34:19.000".compareTo("2022-08-10 14:34:20.481"));

    }
}
