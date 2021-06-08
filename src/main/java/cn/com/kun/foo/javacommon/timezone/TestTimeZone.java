package cn.com.kun.foo.javacommon.timezone;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 判断当前所处时区
 *
 * author:xuyaokun_kzx
 * date:2021/6/4
 * desc:
*/
public class TestTimeZone {

    public static void main(String[] args) {

        //默认的时区
        System.out.println(TimeZone.getDefault().getID());
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            System.out.println(displayTimeZone(TimeZone.getTimeZone(id)));
        }

        System.out.println("\nTotal TimeZone ID " + ids.length);

    }

    /**
     * 输出 时区和标准时区相差的小时数
     * @param tz
     * @return
     */
    private static String displayTimeZone(TimeZone tz) {

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";
        if (hours > 0) {
            result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
        } else {
            result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
        }

        return result;

    }
}
