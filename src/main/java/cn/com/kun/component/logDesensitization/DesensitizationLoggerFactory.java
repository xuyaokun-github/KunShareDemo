package cn.com.kun.component.logDesensitization;

import org.slf4j.Logger;

/**
 * 脱敏有两种：
 * 1.常规脱敏（正则匹配，只脱敏日志中的敏感部分）
 * 2.加密脱敏（将整个日志加密输出，因此需要提供解密操作）
 *
 * author:xuyaokun_kzx
 * date:2022/12/16
 * desc:
*/
public class DesensitizationLoggerFactory {

    /**
     * 生成装饰者
     *
     * @param logger
     * @return
     */
    public static DesensitizationLogger decorator(Logger logger){

        return new DesensitizationLogger(logger);
    }

}
