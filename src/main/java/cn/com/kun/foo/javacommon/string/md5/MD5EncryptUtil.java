package cn.com.kun.foo.javacommon.string.md5;


import org.springframework.util.DigestUtils;

/**
 * 生成MD5
 *
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
public class MD5EncryptUtil {

    public static String md5Encode(String origin) {
        String resultString = null;
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            resultString = new String(md.digest(origin.getBytes()));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        resultString = DigestUtils.md5DigestAsHex(origin.getBytes());

        return resultString;
    }

    public static void main(String[] args) {
        System.out.println(md5Encode("kunghsu"));
    }
}
