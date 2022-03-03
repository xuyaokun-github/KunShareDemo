package cn.com.kun.foo.javacommon.timezone;

import java.util.Calendar;
import java.util.TimeZone;

public class TestCal {

    public static void main(String[] args) {

        Calendar cl= Calendar.getInstance();
        cl.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        //cl.setTimeZone(TimeZone.getDefault());
        System.out.println(cl.getTimeZone());
        System.out.println(cl.getTime());
        System.out.println(cl.get(Calendar.HOUR_OF_DAY));
        System.out.println(cl.get(Calendar.MINUTE));
        System.out.println(cl.get(Calendar.SECOND));

        Calendar cl1=Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        System.out.println(cl1.getTimeZone());
        System.out.println(cl1.getTime());

    }
}
