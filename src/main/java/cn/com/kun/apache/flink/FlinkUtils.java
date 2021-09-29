package cn.com.kun.apache.flink;

import cn.com.kun.common.utils.DateUtils;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.Date;

import static cn.com.kun.common.utils.DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS;

public class FlinkUtils {

    /**
     * 展示窗口起始信息
     *
     * @param window
     * @return
     */
    public static String showWindowInfo(TimeWindow window) {

        Date date1 = new Date(window.getStart());
        Date date2 = new Date(window.getEnd());
        return "start: " + DateUtils.toStr(date1, PATTERN_YYYY_MM_DD_HH_MM_SS)
                + " end:" + DateUtils.toStr(date2, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }


}
