package cn.com.kun.foo.javacommon.string.compress;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * 没有效果，用Base64,大小不减反增
 * Created by xuyaokun On 2022/9/6 15:36
 * @desc:
 */
public class ApacheBase64CompressUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheBase64CompressUtils.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
//    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    /**
     * 使用org.apache.commons.codec.binary.Base64压缩字符串
     * @param str 要压缩的字符串
     * @return
     */
    public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return Base64.encodeBase64String(str.getBytes());
    }

    /**
     * 使用org.apache.commons.codec.binary.Base64解压缩
     * @param compressedStr 压缩字符串
     * @return
     */
    public static String uncompress(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }
        return new String(Base64.decodeBase64(compressedStr));
    }

}
