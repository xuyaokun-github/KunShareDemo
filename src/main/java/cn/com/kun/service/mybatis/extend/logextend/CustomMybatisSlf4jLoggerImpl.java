package cn.com.kun.service.mybatis.extend.logextend;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;

public class CustomMybatisSlf4jLoggerImpl implements Log {

    private final Logger log;

    public CustomMybatisSlf4jLoggerImpl(Logger logger) {
        log = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        log.error(s, e);
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void debug(String s) {
//        log.debug(s);
        log.info(s);
    }

    @Override
    public void trace(String s) {
        log.trace(s);
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

}
