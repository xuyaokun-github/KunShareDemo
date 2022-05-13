package cn.com.kun.foo.javacommon.datatype;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TestDate {

    public static void main(String[] args) {

        /*

        假如想在方法里对date进行修改，应该用setTime方法。
        虽然Date类型是值传递，但是它被设计成了可变类！！它是支持修改的。与String不同，String是不支持修改的。
         */

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2010-01-01 00:00:00", dateTimeFormatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
//        changeDate(date);
        changeDate2(date);
        System.out.println(date);
    }

    private static void changeDate2(Date date) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2020-01-01 00:00:00", dateTimeFormatter);
        Date date2 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        date.setTime(date2.getTime());
        System.out.println("方法内修改后：" + date);
    }

    private static void changeDate(Date date) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2020-01-01 00:00:00", dateTimeFormatter);
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("方法内修改后：" + date);
    }


}
