package cn.com.kun.foo.javacommon.string;

import org.xerial.snappy.Snappy;

import java.io.IOException;

public class TestStringGetBytes {


    public static void main(String[] args) throws IOException {

        System.out.println(System.getProperty("file.encoding"));

        String sourceString = "测试字符串KunghsummmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmY";
//        String sourceString = "测试字符串Kunghsu";
        System.out.println(sourceString.getBytes().length);
        String newString = new String(sourceString.getBytes());
        System.out.println("newString:" + newString + " length:" + newString.length());
        System.out.println(newString.getBytes().length);
        //压缩字节之后，得到一个压缩过后的字节数组
        byte[] byte1 = Snappy.compress(sourceString.getBytes("UTF-8"));
        System.out.println("byte1:" + byte1.length);//30
        String newString2 = new String(byte1);
        System.out.println("newString2:" + newString2 + " length:" + newString2.length());
        System.out.println(newString2.getBytes().length);//32
        byte[] byte2 = new String(byte1).getBytes();
        //可见一个byte 重新new String之后再得到byte[],内容会发生变化
        System.out.println("byte2:" + byte2.length);//32
        //解压缩(因为字节数发生了变化，解压会提示失败)
        byte[] byte3 =  Snappy.uncompress(newString2.getBytes());
        String newString3 = new String(byte3);
        System.out.println("newString3:" + newString3);
        System.out.println(newString3.getBytes().length);

    }

}
