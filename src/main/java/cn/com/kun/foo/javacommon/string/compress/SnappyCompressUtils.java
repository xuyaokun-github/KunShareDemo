package cn.com.kun.foo.javacommon.string.compress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 亲测失败（原因是编码问题，默认的压缩逻辑底层用的并不是UTF-8编码）
 * （反例）
 *
 * Created by xuyaokun On 2022/9/7 0:19
 * @desc:
 *
 * 报错
 * java.io.IOException: FAILED_TO_UNCOMPRESS(5)
at org.xerial.snappy.SnappyNative.throw_error(SnappyNative.java:98)
at org.xerial.snappy.SnappyNative.rawUncompress(Native Method)
at org.xerial.snappy.Snappy.rawUncompress(Snappy.java:474)
at org.xerial.snappy.Snappy.uncompress(Snappy.java:513)
at org.xerial.snappy.Snappy.uncompress(Snappy.java:488)
at cn.com.kun.foo.javacommon.string.compress.SnappyCompressUtils.uncompress(SnappyCompressUtils.java:47)
at cn.com.kun.foo.javacommon.string.compress.SnappyCompressUtils.uncompress(SnappyCompressUtils.java:38)

 */
public class SnappyCompressUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnappyCompressUtils.class);

//        private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    public static String compress(String input) {
//        try {
//            return new String(compress(input.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
        byte[] by = new byte[0];
        try {
            by = Snappy.compress(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(by, StandardCharsets.UTF_8);
    }

    public static byte[] compress(byte srcBytes[]) throws IOException {
        return Snappy.compress(srcBytes);
    }

    public static String uncompress(String input) {

//        try {
//            return new String(uncompress(input.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
        byte[] by = input.getBytes(StandardCharsets.UTF_8);
        try {
            return Snappy.uncompressString(by, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] uncompress(byte[] bytes) throws IOException {
        return Snappy.uncompress(bytes);
    }


}
