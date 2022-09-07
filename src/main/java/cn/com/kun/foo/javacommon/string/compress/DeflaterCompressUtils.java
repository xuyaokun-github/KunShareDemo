package cn.com.kun.foo.javacommon.string.compress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 *
 * Created by xuyaokun On 2022/9/6 23:56
 * @desc:
 */
public class DeflaterCompressUtils {

    // 解压
    public static String uncompress(String encdata) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InflaterOutputStream zos = new InflaterOutputStream(bos);
            zos.write(convertFromBase64(encdata));
            zos.close();
            return new String(bos.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 压缩
    private static byte[] convertFromBase64(String encdata) {
        byte[] compressed = null;
        try {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(encdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressed;
    }

    /**
     * TODO 改成try-catch-resource写法
     * @param data
     * @return
     */
    public static String compress(String data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //这里用的是默认的压缩级别
            //假如需要自定义级别，可以自行创建Deflater对象，调用重载的DeflaterOutputStream构造函数
            DeflaterOutputStream zos = new DeflaterOutputStream(bos);
            zos.write(data.getBytes());
            zos.close();
            return new String(convertToBase64(bos.toByteArray()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String convertToBase64(byte[] byteArray) {
        return new sun.misc.BASE64Encoder().encode(byteArray);
    }
}
