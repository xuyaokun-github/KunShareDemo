package cn.com.kun.framework.log4j2;

import cn.com.kun.common.utils.LogUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.logDesensitization.DesensitizationLogger;
import cn.com.kun.component.logDesensitization.DesensitizationLoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/log4j2-demo")
@RestController
public class Log4j2DemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(Log4j2DemoController.class);

    private final static DesensitizationLogger DLOGGER = DesensitizationLoggerFactory.decorator(LOGGER);

    @Autowired
    private LoggingSystem loggingSystem;

    @GetMapping("/print")
    public ResultVo print(){

        LOGGER.debug("我是Log4j2DemoController的debug日志");
        LOGGER.info("我是Log4j2DemoController的info日志");
        LOGGER.error("我是Log4j2DemoController的error日志");

        int source = 0;
        String target = "xyk" + (char)(source) + "kunghsu";
        LOGGER.error("target:{}", target);

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/desensitization")
    public ResultVo desensitization(){

        //常规用法
        DLOGGER.info("kunghsu");
        DLOGGER.info("kunghsu:{}", System.currentTimeMillis());
        //脱敏用法
        DLOGGER.infoDesensitize("kunghsu:{}", System.currentTimeMillis());
        DLOGGER.infoDesensitize("kunghsu");

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/desensitization2")
    public ResultVo desensitization2(){

        ResultVo resultVo = ResultVo.valueOfSuccess(System.currentTimeMillis());
        //常规用法
        DLOGGER.info("kunghsu:{}", resultVo);
        //脱敏用法
        DLOGGER.infoByDst("kunghsu:{}", resultVo);

        return ResultVo.valueOfSuccess();
    }
    /**
     * 动态修改日志级别
     * 在springboot中可以使用这个方法，使用的是springboot提供的类
     * @return
     */
    @GetMapping("/change")
    public ResultVo change(){

        List<LoggerConfiguration> loggerConfigurations = loggingSystem.getLoggerConfigurations();
//        Logger logger = (Logger) LoggerFactory.getLogger(Log4j2DemoController.class);

        //假如是在springboot中，可以用这种方法，动态调整级别
        loggingSystem.setLogLevel("cn.com.kun.framework.log4j2", LogLevel.ERROR);

        return ResultVo.valueOfSuccess();
    }

    /**
     * 动态修改日志级别
     * 在普通的Java工程就可以用的方法
     * @return
     */
    @GetMapping("/change2")
    public ResultVo change2(){

        //参考自org.springframework.boot.logging.log4j2.Log4J2LoggingSystem.setLogLevel
        String loggerName = "cn.com.kun.framework.log4j2";
        LoggerConfig loggerConfig = getLoggerConfig(loggerName);
        if (loggerConfig == null) {
            loggerConfig = new LoggerConfig(loggerName, Level.ERROR, true);
            getLoggerContext().getConfiguration().addLogger(loggerName, loggerConfig);
        } else {
            loggerConfig.setLevel(Level.ERROR);
        }
        getLoggerContext().updateLoggers();

        return ResultVo.valueOfSuccess();
    }

    /**
     * 动态修改日志级别
     * 在普通的Java工程就可以用的方法
     * @return
     */
    @GetMapping("/change3")
    public ResultVo change3(){

        //参考自org.springframework.boot.logging.log4j2.Log4J2LoggingSystem.setLogLevel
        String loggerName = "cn.com.kun.framework.log4j2";
        LogUtils.setLevel(loggerName, Level.ERROR);

        return ResultVo.valueOfSuccess();
    }

    private LoggerContext getLoggerContext() {
        //原理其实就是获取到Lo4j2的单例，进行日志级别的调整
        return (LoggerContext) LogManager.getContext(false);
    }

    private LoggerConfig getLoggerConfig(String loggerName) {
        return getLoggerContext().getConfiguration().getLoggers().get(loggerName);
    }


}
