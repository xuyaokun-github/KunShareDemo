package cn.com.kun.foo.javacommon.datatype;

import java.util.Date;
import java.util.TimeZone;

public class TestUTCDate {

    public static void main(String[] args) {

        Date date = new Date();
        System.out.println(date);
        System.out.println(TimeZone.getDefault());
    }
}
