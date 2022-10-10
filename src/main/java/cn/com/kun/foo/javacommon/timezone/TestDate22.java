package cn.com.kun.foo.javacommon.timezone;

import java.text.ParseException;
import java.util.Date;

public class TestDate22 {

    public static void main(String[] args) throws ParseException {

        Date date1 = new Date(0L);
        // 0时区的0点,东8区的8点
        Date date2 = new Date(3600*1000L);
        //0时区的1点,东8区的9点
        Date date3 = new Date(7200L*1000);
        //0时区的3点,东8区的11点
        Date date4 = new Date(date1.getTime() + date2.getTime());
        Date date5 = new Date(date1.getTime() + date2.getTime() + date3.getTime());
        Date date6 = new Date(date1.getTime());

        System.out.println(date4);
        System.out.println(date5);
        System.out.println(date1);
        System.out.println(date6);

        System.out.println("-----------------");
        Date date10 = new Date();
        System.out.println(date10);
        System.out.println(date10.getTime());
        Date date11 = new Date(date10.getTime());
        System.out.println(date11);//输出和date10是一样的
        System.out.println(date11.getTime());//输出和date10是一样的


    }
}
