package cn.com.kun.foo.javacommon.string.compress;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.*;

public class StringCompressTestDemo {

    public static void main(String[] args) {

        //生成字符串
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            strList.add(UUID.randomUUID().toString());
        }
        Map<String, String> map = new HashMap<>();
        strList.forEach(str->{
            map.put(str, str);
        });
        map.put("中文", "中文值");

        String sourceString = JacksonUtils.toJSONString(map);

//        sourceString = "KKKKKKKKKKKKKKKKKKKK";

        System.out.println("原字符串字节数：" + sourceString.getBytes().length);
        System.out.println("原字符串字符数：" + sourceString.length());

        System.out.println("--------------------------------- gzip ---------------------------------------------");

        //使用压缩工具类
        long start = System.currentTimeMillis();
        String targetString = GzipCompressUtils.compress(sourceString);
        System.out.println("压缩耗时：" + (System.currentTimeMillis() - start));
        System.out.println("压缩后字符串字节数：" + targetString.getBytes().length);
        System.out.println("压缩后字符串字符数：" + targetString.length());

        //解压缩
        String newSourceString = GzipCompressUtils.uncompress(targetString);
        System.out.println("解压后字符串字节数：" + newSourceString.getBytes().length);
        System.out.println("解压后字符串字符数：" + newSourceString.length());
//        System.out.println(newSourceString);

//        System.out.println("----------------------------------- apache base64 -------------------------------------------");
//
//        //使用压缩工具类
//        start = System.currentTimeMillis();
//        targetString = ApacheBase64CompressUtils.compress(sourceString);
//        System.out.println("压缩耗时：" + (System.currentTimeMillis() - start));
//        System.out.println("压缩后字符串字节数：" + targetString.getBytes().length);
//        System.out.println("压缩后字符串字符数：" + targetString.length());
//
//        //解压缩
//        newSourceString = ApacheBase64CompressUtils.uncompress(targetString);
//        System.out.println("解压后字符串字节数：" + newSourceString.getBytes().length);
//        System.out.println("解压后字符串字符数：" + newSourceString.length());

        System.out.println("----------------------------------- gzip -------------------------------------------");

        //使用压缩工具类
        start = System.currentTimeMillis();
        targetString = GzipCompressUtils.compress(sourceString);
        System.out.println("压缩耗时：" + (System.currentTimeMillis() - start));
        System.out.println("压缩后字符串字节数：" + targetString.getBytes().length);
        System.out.println("压缩后字符串字符数：" + targetString.length());

        //解压缩
        newSourceString = GzipCompressUtils.uncompress(targetString);
        System.out.println("解压后字符串字节数：" + newSourceString.getBytes().length);
        System.out.println("解压后字符串字符数：" + newSourceString.length());

//        System.out.println("----------------------------------- bzip2 -------------------------------------------");
//
//        //使用压缩工具类
//        start = System.currentTimeMillis();
//        targetString = Bzip2CompressUtils.compress(sourceString);
//        System.out.println("压缩耗时：" + (System.currentTimeMillis() - start));
//        System.out.println("压缩后字符串字节数：" + targetString.getBytes().length);
//        System.out.println("压缩后字符串字符数：" + targetString.length());
//
//        //解压缩
//        newSourceString = Bzip2CompressUtils.uncompress(targetString);
//        System.out.println("解压后字符串字节数：" + newSourceString.getBytes().length);
//        System.out.println("解压后字符串字符数：" + newSourceString.length());

        System.out.println("----------------------------------- Deflater -------------------------------------------");

        //使用压缩工具类
        start = System.currentTimeMillis();
        targetString = DeflaterCompressUtils.compress(sourceString);
        System.out.println("压缩耗时：" + (System.currentTimeMillis() - start));
        System.out.println("压缩后字符串字节数：" + targetString.getBytes().length);
        System.out.println("压缩后字符串字符数：" + targetString.length());

        //解压缩
        newSourceString = DeflaterCompressUtils.uncompress(targetString);
        System.out.println("解压后字符串字节数：" + newSourceString.getBytes().length);
        System.out.println("解压后字符串字符数：" + newSourceString.length());

        System.out.println("----------------------------------- Snappy -------------------------------------------");

        //使用压缩工具类
        start = System.currentTimeMillis();
        targetString = SnappyUtils.compress(sourceString);
        System.out.println("压缩耗时：" + (System.currentTimeMillis() - start));
        System.out.println("压缩后字符串字节数：" + targetString.getBytes().length);
        System.out.println("压缩后字符串字符数：" + targetString.length());

        //解压缩
        newSourceString = SnappyUtils.uncompress(targetString);
        System.out.println("解压后字符串字节数：" + newSourceString.getBytes().length);
        System.out.println("解压后字符串字符数：" + newSourceString.length());

    }


}
