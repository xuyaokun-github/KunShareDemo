package cn.com.kun.foo.javacommon.timezone;

import java.util.Calendar;
import java.util.Date;

public class TestDate {

    public static void main(String[] args) {

//        DateUtils.toDate("", PATTERN_YYYY_MM_DD_HH_MM_SS);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR);
        int hour2 = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);
        System.out.println(hour2);

    }

}
