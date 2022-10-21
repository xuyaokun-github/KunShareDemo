package cn.com.kun.framework.quartz.cron;

import org.apache.commons.lang.StringUtils;

/**
 * CRON处理工具类
 * 时间表达式翻译工具类
 *
 */
public class CronUtil {

    public static void main(String[] args) {

        System.out.println(translateToChinese("3/8 * * * * ? *"));

    }

    /**
     * CRON表达式翻译为中文描述
     * <p>复杂表达式和部分通配符不支持，不处理年(第七段)</p>
     *
     * @param cronStr cron表达式
     * @return 中文描述
     */
    public static String translateToChinese(String cronStr) {
        if (StringUtils.isBlank(cronStr)) {
            throw new IllegalArgumentException("cron表达式为空");
        }

        String[] cronArray = cronStr.split(" ");
        // 表达式到年会有7段， 至少6段
        if (cronArray.length != 6 && cronArray.length != 7) {
            throw new IllegalArgumentException("cron表达式格式错误");
        }

        String secondCron = cronArray[0];
        String minuteCron = cronArray[1];
        String hourCron = cronArray[2];
        String dayCron = cronArray[3];
        String monthCron = cronArray[4];
        String weekCron = cronArray[5];

        StringBuilder result = new StringBuilder();
        // 解析月
        if (!monthCron.equals("*") && !monthCron.equals("?")) {
            if (monthCron.contains("/")) {
                result.append("从")
                        .append(monthCron.split("/")[0])
                        .append("号开始")
                        .append(",每")
                        .append(monthCron.split("/")[1])
                        .append("月");
            } else {
                result.append("每年").append(monthCron).append("月");
            }
        }

        // 解析周
        boolean hasWeekCron = false;
        if (!weekCron.equals("*") && !weekCron.equals("?")) {
            hasWeekCron = true;
            if (weekCron.contains(",")) {
                result.append("每周的第").append(weekCron).append("天");
            } else {
                result.append("每周");
                char[] tmpArray = weekCron.toCharArray();
                for (char tmp : tmpArray) {
                    switch (tmp) {
                        case '1':
                            result.append("日");
                            break;
                        case '2':
                            result.append("一");
                            break;
                        case '3':
                            result.append("二");
                            break;
                        case '4':
                            result.append("三");
                            break;
                        case '5':
                            result.append("四");
                            break;
                        case '6':
                            result.append("五");
                            break;
                        case '7':
                            result.append("六");
                            break;
                        default:
                            result.append(tmp);
                            break;
                    }
                }
            }
        }

        // 解析日
        if (!dayCron.equals("?") && !"*".equals(dayCron)) {
            if (hasWeekCron) {
                throw new IllegalArgumentException("表达式错误，不允许同时存在指定日和指定星期");
            }
            if (dayCron.contains("/")) {
                result.append("每月从第")
                        .append(dayCron.split("/")[0])
                        .append("天开始")
                        .append(",每")
                        .append(dayCron.split("/")[1])
                        .append("天");
            } else {
                result.append("每月第").append(dayCron).append("天");
            }
        }

        // 解析时
        if (!hourCron.equals("*")) {
            if (hourCron.contains("/")) {
                result.append("从")
                        .append(hourCron.split("/")[0])
                        .append("点开始")
                        .append(",每")
                        .append(hourCron.split("/")[1])
                        .append("小时");
            } else {
                if (!(result.toString().length() > 0)) {
                    result.append("每天").append(hourCron).append("点");
                }
            }
        }

        // 解析分
        if (!minuteCron.equals("*") && !minuteCron.equals("0")) {
            if (minuteCron.contains("/")) {
                result.append("从第")
                        .append(minuteCron.split("/")[0])
                        .append("分开始").append(",每")
                        .append(minuteCron.split("/")[1])
                        .append("分");
            } else {
                result.append(minuteCron).append("分");
            }
        }

        // 解析秒
        if (!secondCron.equals("*") && !secondCron.equals("0")) {
            if (secondCron.contains("/")) {
                result.append("从第")
                        .append(secondCron.split("/")[0])
                        .append("秒开始")
                        .append(",每")
                        .append(secondCron.split("/")[1])
                        .append("秒");
            } else {
                result.append(secondCron).append("分");
            }
        }

        if (StringUtils.isNotBlank(result.toString())) {
            result.append("执行一次");
        }
        return result.toString();
    }

}