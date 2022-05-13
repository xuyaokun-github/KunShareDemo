package cn.com.kun.common.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * 日志辅助工具类
 *
 * author:xuyaokun_kzx
 * date:2022/4/18
 * desc:
*/
public class LogUtils {

    /**
     * 动态设置Logger级别
     * @param loggerName （传入的是包名，通常是父包名）
     * @param level
     */
    public static void setLevel(String loggerName, Level level) {

        LoggerConfig loggerConfig = getLoggerConfig(loggerName);
        if (loggerConfig == null) {
            loggerConfig = new LoggerConfig(loggerName, level, true);
            getLoggerContext().getConfiguration().addLogger(loggerName, loggerConfig);
        } else {
            loggerConfig.setLevel(level);
        }
        getLoggerContext().updateLoggers();
    }

    private static LoggerContext getLoggerContext() {
        //原理其实就是获取到Lo4j2的单例，进行日志级别的调整
        return (LoggerContext) LogManager.getContext(false);
    }

    private static LoggerConfig getLoggerConfig(String loggerName) {
        return getLoggerContext().getConfiguration().getLoggers().get(loggerName);
    }

}
