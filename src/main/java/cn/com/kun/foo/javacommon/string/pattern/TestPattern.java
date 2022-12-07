package cn.com.kun.foo.javacommon.string.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern {

    public static void main(String[] args) {

        String source = "尊敬的$q$,,,,您的账号：$w$,,,,,消费：$e$";

        System.out.println("----------------------测试 \\\\$(.*)\\\\$----------------------");

        Pattern pattern = Pattern.compile("\\$(.*)\\$");//这两个有区别
        Matcher matcher = pattern.matcher(source);
//        System.out.println(matcher.find());
//        String group = matcher.group();
        while(matcher.find()) {
            System.out.println(matcher.group());//group() 默认就是 group(0)
//            System.out.println(matcher.group(1));
        }

        System.out.println("----------------------测试 \\\\$(.*?)\\\\$----------------------");
        Pattern pattern2 = Pattern.compile("\\$(.*?)\\$");
        Matcher matcher2 = pattern2.matcher(source);
        while(matcher2.find()) {
            System.out.println(matcher2.group());//group() 默认就是 group(0)
//            System.out.println(matcher2.group(1));
        }

        System.out.println("-----------m.group(1)不包括这两个字符-----------");

        String filetext = "//@张小名: 25分//@李小花: 43分//@王力: 100分";
        Pattern p = Pattern.compile("\\@(.*?)\\:");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher m = p.matcher(filetext);
        while(m.find()) {
            System.out.println(m.group(1));//m.group(1)不包括这两个字符
        }
    }

}
