package cn.com.kun.component.logDesensitization;

import org.slf4j.Logger;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/16
 * desc:
*/
public class DesensitizationLogger {

    /**
     * 被装饰者
     */
    private final Logger logger;

    public DesensitizationLogger(Logger logger) {
        this.logger = logger;
    }

    public void info(String format, Object... arguments){
        this.logger.info(format, arguments);
    }

    /**
     *
     * @param format
     * @param arguments
     */
    public void infoDesensitize(String format, Object... arguments){

        //将原文转为密文，然后打印
        try {
            String source = LogDesensitizationUtils.buildMessage(format, arguments);
            String encryptString = LogDesensitizationUtils.encrypt(source);
            this.logger.info(encryptString);
        }catch (Exception e){
            this.logger.error("脱敏日志打印异常", e);
            this.logger.info(format, arguments);
        }
    }

    /**
     *
     */
    public void infoDesensitize(String message){

        //将原文转为密文，然后打印
        try {
            String encryptString = LogDesensitizationUtils.encrypt(message);
            this.logger.info(encryptString);
        }catch (Exception e){
            this.logger.error("脱敏日志打印异常", e);
            this.logger.info(message);
        }
    }


    public void warn(String format, Object... arguments){
        this.logger.warn(format, arguments);
    }

    public void error(String format, Object... arguments){
        this.logger.error(format, arguments);
    }

}
