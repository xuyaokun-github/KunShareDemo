package cn.com.kun.foo.javacommon.base64;

import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64工具类-JDK实现
 *
 * author:xuyaokun_kzx
 * date:2023/3/14
 * desc:
*/
public class Base64JdkImplUtils {

    private static Base64.Encoder ENCODER = Base64.getEncoder();
    private static Base64.Decoder DECODER = Base64.getDecoder();

    public static void main(String[] args) {

        BASE64Encoder base64Encoder = new BASE64Encoder();
        String sourceString = base64Encoder.encode("kunghsu深圳市福田区kunghsu发的发发发沙发沙发沙发沙发发送到发放".getBytes(StandardCharsets.UTF_8));
        //解码失败
        System.out.println(DECODER.decode(sourceString));
        sourceString = base64Encoder.encode("kunghsu深圳市福田区kunghsu".getBytes());
        //解码失败
        System.out.println(DECODER.decode(sourceString));

        //成功
//        sourceString = encrypt("kunghsu深圳市福田区kunghsu");
//        System.out.println(decrypt(sourceString));
    }

    /**
     * BASE64加密
     */
    public static String encrypt(String str) {
        if (str == null) return null;
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

    /**
     * BASE64解密
     */
    public static String decrypt(String key) {
        if (key == null) return null;
        byte[] decoded = Base64.getDecoder().decode(key);
        String decodeStr = new String(decoded);
        return decodeStr;
    }

}