package cn.com.kun.component.logDesensitization;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.nio.charset.StandardCharsets;

/**
 * author:xuyaokun_kzx
 * date:2022/12/16
 * desc:
*/
public class LogDesensitizationUtils {

    /**
     * 假如要做动态设置开关，要将所有DesensitizationLogger缓存起来，然后针对单个DesensitizationLogger修改开关
     *
     */
    private static boolean desensitizationEnable = true;

    private static BuildMessageFunction buildMessageFunction;

    static {
        if (existLog4jUtilsPackage()){
            /*
                org.apache.logging.log4j.message.ParameterizedMessage
                org.apache.logging.log4j.message.ParameterizedMessage#getFormattedMessage()
                它运用了StringBuilder线程缓存
             */
            buildMessageFunction = new BuildMessageFunction() {
                @Override
                public String buildMessage(String format, Object... arguments) {
                    ParameterizedMessage parameterizedMessage = new ParameterizedMessage(format, arguments);
                    return parameterizedMessage.getFormattedMessage();
                }
            };
        }else {
            //其他实现 TODO
            buildMessageFunction = new BuildMessageFunction() {
                @Override
                public String buildMessage(String format, Object... arguments) {
                    //自定义实现
                    return LogMessageParser.parse1(format, arguments);
                }
            };
        }
    }

    private static boolean existLog4jUtilsPackage() {

        try {
            Class.forName("org.apache.logging.log4j.message.ParameterizedMessage");
            return true;
        } catch (ClassNotFoundException e) {
            //对类名的判断,异常则说明不存在
        }
        return false;
    }

    public static void close() {
        desensitizationEnable = false;
    }

    public static boolean isEnabled() {
        return desensitizationEnable;
    }


    private interface BuildMessageFunction {
        String buildMessage(String format, Object... arguments);
    }

    public static String buildMessage(String format, Object... arguments){
        return buildMessageFunction.buildMessage(format, arguments);
    }


    public static String encrypt(String source) {
        return Base64.encodeBase64String(source.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解密方法
     *
     * @param source
     * @return
     */
    public static String decrypt(String source){
        return new String(Base64.decodeBase64(source), StandardCharsets.UTF_8);
    }



}
