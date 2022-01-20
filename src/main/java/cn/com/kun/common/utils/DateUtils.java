package cn.com.kun.common.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获取date对象
     * @param sourceStr 2021-06-01 14:07:02.100
     * @param pattern yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public static Date toDate(String sourceStr, String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(sourceStr, dateTimeFormatter);
        Date date =
                Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public static String toStr(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String now(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.format(now);
    }

    public static String nowWithNoSymbol(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return dateTimeFormatter.format(now);
    }

    /**
     * 使用java 8的Period的对象计算两个LocalDate对象的时间差，严格按照年、月、日计算，如：2018-03-12 与 2014-05-23 相差 3 年 9 个月 17 天
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public static void calculateTimeDifferenceByPeriod(int year, Month month, int dayOfMonth) {
        LocalDate today = LocalDate.now();
        System.out.println("Today：" + today);
        LocalDate oldDate = LocalDate.of(year, month, dayOfMonth);
        System.out.println("OldDate：" + oldDate);

        Period p = Period.between(oldDate, today);
        System.out.printf("目标日期距离今天的时间差：%d 年 %d 个月 %d 天\n", p.getYears(), p.getMonths(), p.getDays());
    }

    public static long betweenDays(String startDate, String endDate) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate oldDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate today = LocalDate.parse(endDate, dateTimeFormatter);
        Period p = Period.between(oldDate, today);
        long daysDiff = ChronoUnit.DAYS.between(oldDate, today);
        return daysDiff;
    }

    public static void main(String[] args) {
//        System.out.println(toDate("2021-06-01 14:07:02.100", "yyyy-MM-dd HH:mm:ss.SSS"));
//        calculateTimeDifferenceByPeriod(2022, Month.JANUARY, 17);
        System.out.println(betweenDays("20210601", "20230601"));
    }

}
