package cn.com.kun.foo.javacommon.string.compress;

import cn.com.kun.common.utils.JacksonUtils;
import org.apache.commons.codec.binary.Base64;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.*;


/**
 * 亲测成功（在window上必须用Base64转义之后再向上层返回）
 *
 * Created by xuyaokun On 2022/9/7 13:49
 * @desc:
 */
public class SnappyUtils {

    public static void main(String[] args) {

//        String source = "KKKKKKKKKKKKKKKKKKKK";
        //生成字符串
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            strList.add(UUID.randomUUID().toString());
        }
        Map<String, String> map = new HashMap<>();
        strList.forEach(str->{
            map.put(str, str);
        });
//        map.put("中文", "中文值");

        String source = JacksonUtils.toJSONString(map);

        byte[] compressByte = SnappyUtils.compressHtml(source);
        System.out.println(compressByte.length);
        try {
            //IDEA默认用的就是"UTF-8"，但是window系统默认用的不是"UTF-8"
//            compressByte = new String(compressByte, "UTF-8").getBytes("UTF-8");
            //问题就发生在这里，假如重新new String 会改变字节数
//            compressByte = new String(compressByte).getBytes();
            System.out.println(compressByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String target = SnappyUtils.decompressHtml(compressByte);
        System.out.println("字节方式解压: " + target);

        String compressStr = SnappyUtils.compress(source);
        String target2 = SnappyUtils.uncompress(compressStr);
        System.out.println(target2);
    }

    public static String compress(String input) {
        try {
            //这样子返回，后续解压会出问题（因为压缩逻辑用的编码规则不是UTF-8,而是根据系统选择的编码规则）
//            return new String(compressHtml(input), "UTF-8");

            //解决办法是先将字节转成Base64，再返回。解压时，拿到的是Base64,转换得到待解压缩的字节
            return Base64.encodeBase64String(compressHtml(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public static byte[] compressHtml(String html) {
        try {
            return Snappy.compress(html.getBytes("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();

            return null;

        }

    }

    public static String uncompress(String input) {

        try {
            return decompressHtml(Base64.decodeBase64(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String decompressHtml(byte[] bytes) {
        try {
            return new String(Snappy.uncompress(bytes), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();

            return null;

        }

    }

}
