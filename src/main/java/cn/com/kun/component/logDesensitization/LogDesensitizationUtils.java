package cn.com.kun.component.logDesensitization;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * author:xuyaokun_kzx
 * date:2022/12/16
 * desc:
*/
public class LogDesensitizationUtils {

    public static void main(String[] args) throws UnsupportedEncodingException {

        new ParameterizedMessage("message:{}", "params");
        String str = "";
        for (int i = 0; i < 1000; i++) {
            str += (i + "");
        }
        System.out.println(buildMessage("kunghsu:{}", System.currentTimeMillis()));

        System.out.println("-------------------");
        System.out.println(new String(Base64.decodeBase64("a3VuZ2hzdToxNjcxMjAyMDI0NDkx"), "UTF-8"));
    }

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
