package cn.com.kun.component.logDesensitization.other;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.io.UnsupportedEncodingException;

public class LogDesensitizationTest {

    public static void main(String[] args) throws UnsupportedEncodingException {

        new ParameterizedMessage("message:{}", "params");
        String str = "";
        for (int i = 0; i < 1000; i++) {
            str += (i + "");
        }
//        System.out.println(buildMessage("kunghsu:{}", System.currentTimeMillis()));

        System.out.println("-------------------");
        System.out.println(new String(Base64.decodeBase64("a3VuZ2hzdToxNjcxMjAyMDI0NDkx"), "UTF-8"));
    }
}
