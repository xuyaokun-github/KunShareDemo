package cn.com.kun.foo.javacommon.string.compress;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * bzip2 耗时较长，压缩率一般。综合比较，比不过gzip
 * 解压时还有ArrayIndexOutOfBoundsException异常
 *
 * Created by xuyaokun On 2022/9/6 15:36
 * @desc:
 */
public class Bzip2CompressUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bzip2CompressUtils.class);

//    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    public static String compress(String input) {
        try {
            return new String(compress(input.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] compress(byte srcBytes[]) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bcos = new BZip2CompressorOutputStream(out);
        bcos.write(srcBytes);
        bcos.close();
        return out.toByteArray();
    }

    public static String uncompress(String input) {

        return new String(uncompress(input.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
    }


    public static byte[] uncompress(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            BZip2CompressorInputStream ungzip = new BZip2CompressorInputStream(in);
            byte[] buffer = new byte[2048];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

}
