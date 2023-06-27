package cn.com.kun.foo.javacommon.string.md5.longdemo;

import org.springframework.util.DigestUtils;

import java.nio.ByteBuffer;

/**
 *@Description:
 */
public class TestMD5ToLong {

    private static ByteBuffer buffer = ByteBuffer.allocate(8);
    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }


    /**
     * @param md5L16
     * @return
     * @Date:2014-3-18
     * @Author:lulei
     * @Description: 将16位的md5转化为long值
     */
    public static long parseMd5L16ToLong(String md5L16){
        if (md5L16 == null) {
            throw new NumberFormatException("null");
        }
        md5L16 = md5L16.toLowerCase();
        byte[] bA = md5L16.getBytes();
        long re = 0L;
        for (int i = 0; i < bA.length; i++) {
            //加下一位的字符时，先将前面字符计算的结果左移4位
            re <<= 4;
            //0-9数组
            byte b = (byte) (bA[i] - 48);
            //A-F字母
            if (b > 9) {
                b = (byte) (b - 39);
            }
            //非16进制的字符
            if (b > 15 || b < 0) {
                throw new NumberFormatException("For input string '" + md5L16);
            }
            re += b;
        }
        return re;
    }

    /**
     * @param str16
     * @return
     * @Date:2014-3-18
     * @Author:lulei
     * @Description: 将16进制的字符串转化为long值
     */
    public static long parseString16ToLong(String str16){
        if (str16 == null) {
            throw new NumberFormatException("null");
        }
        //先转化为小写
        str16 = str16.toLowerCase();
        //如果字符串以0x开头，去掉0x
        str16 = str16.startsWith("0x") ? str16.substring(2) : str16;
        if (str16.length() > 16) {
            throw new NumberFormatException("For input string '" + str16 + "' is to long");
        }
        return parseMd5L16ToLong(str16);
    }

    public static void main(String[] args) {
//        System.out.println(parseString16ToLong("0x1fffff"));
//        System.out.println(Long.valueOf("7fffffffffff", 16));
        System.out.println(Long.MAX_VALUE);

        String str = new String("kunghsu");
        System.out.println(str.hashCode());
        String str1 = new String("kunghsu");
        System.out.println(str1.hashCode());

        System.out.println(DigestUtils.md5DigestAsHex(str.getBytes()));
//        System.out.println(parseString16ToLong(DigestUtils.md5DigestAsHex(str.getBytes())));
//        System.out.println(Long.valueOf(DigestUtils.md5DigestAsHex(str.getBytes()), 16));

        System.out.println(bytesToLong(DigestUtils.md5DigestAsHex(str.getBytes()).getBytes()));
    }

}
