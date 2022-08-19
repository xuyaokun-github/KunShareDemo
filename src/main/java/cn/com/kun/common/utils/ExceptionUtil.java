package cn.com.kun.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * author:xuyaokun_kzx
 * date:2022/8/19
 * desc:
*/
public class ExceptionUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

    /**
     * 打印异常信息
     */
    public static String getMessage(Exception e) {

        String swStr = null;
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            swStr = sw.toString();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return swStr;
    }
}
