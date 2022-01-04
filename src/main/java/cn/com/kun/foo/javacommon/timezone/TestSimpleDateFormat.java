package cn.com.kun.foo.javacommon.timezone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 关于SimpleDateFormat的一个经典问题
 * author:xuyaokun_kzx
 * date:2021/12/24
 * desc:
*/
public class TestSimpleDateFormat {

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String str3 = "1927-12-31 23:54:07";
//        String str4 = "1927-12-31 23:54:08";
        String str3 = "1900-01-01 08:05:42";
        String str4 = "1900-01-01 08:05:43";
        Date sDt3 = sf.parse(str3);
        Date sDt4 = sf.parse(str4);
        long ld3 = sDt3.getTime() /1000;
        long ld4 = sDt4.getTime() /1000;
        //输出竟然是-342
        System.out.println(ld4-ld3);
    }

    private boolean checkTimeIsWork() {
        boolean flag = false;
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            Date testdate = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(testdate);
            int x = cal.get(Calendar.DAY_OF_WEEK);
            switch (x) {
                case Calendar.MONDAY:
                case Calendar.TUESDAY:
                case Calendar.WEDNESDAY:
                case Calendar.THURSDAY:
                case Calendar.FRIDAY:
                    flag = false;
                    break;
                case Calendar.SATURDAY:
                case Calendar.SUNDAY:
                    flag = true;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
