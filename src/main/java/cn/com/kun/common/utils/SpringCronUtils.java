package cn.com.kun.common.utils;

import com.google.common.collect.Lists;
import org.quartz.CronExpression;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpringCronUtils {

    /**
     * 获取下次时间点列表
     * @param cron 表达式
     * @param count 需要计算的数量
     * @return 返回日期集合
     */
    public static List<Date> calNextPoint(String cron, int count) {
        return calNextPoint(cron, new Date(), count);
    }

    /**
     * 获取下次时间点列表
     * @param cron 表达式
     * @param date 当前日期(这个是计算的基点)
     * @param count 需要计算的数量
     * @return 返回日期集合
     */
    public static List<Date> calNextPoint(String cron, Date date,int count) {
        List<Date> points = new ArrayList<>();
            //CronSequenceGenerator不支持7位表达式，所以用org.quartz.CronExpression
//            CronSequenceGenerator csg = new CronSequenceGenerator(cron);
            try {
                CronExpression cronExpression = new CronExpression(cron);
                Date nextDate = date;
                for(int i = 0 ; i < count; i++) {
                    //获取下一次执行时间,CronSequenceGenerator不支持7位表达式
//                    nextDate = nextPoint(csg, nextDate, cron);
                    nextDate = nextPoint(cronExpression, nextDate);
                    //假如为空，说明没有下一次执行时间了，退出
                    if (nextDate == null){
                        return points;
                    }
                    points.add(nextDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return Lists.newArrayList();
            }

        return points;
    }

    /**
     *  计算下次时间点
     * @param csg
     * @param date
     * @return
     */
    public static Date nextPoint(CronSequenceGenerator csg, Date date, String cron) {
        if(CronSequenceGenerator.isValidExpression(cron)) {
            return csg.next(date);
        }else {
            return null;
        }

    }

    public static Date nextPoint(CronExpression cronExpression, Date date) {
        return cronExpression.getNextValidTimeAfter(date);
    }
}
