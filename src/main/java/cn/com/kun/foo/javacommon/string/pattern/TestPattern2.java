package cn.com.kun.foo.javacommon.string.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern2 {

    public static void main(String[] args) {

        String source = "xxxxFUNC_SEX(${sex1})xxxxxxxxxFUNC_SEX(${sex1})xxx";
//        Pattern pattern = Pattern.compile("FUNC_SEX[(][^)]*[)]");
        Pattern pattern = Pattern.compile("FUNC_SEX[(].*[)]");
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()) {
            System.out.println(matcher.group());//group() 默认就是 group(0)
        }

        //
        System.out.println("------------------------------");
//        String source2 = "Input: FUNC_SEX(abcd), Output: abc";
        String source2 = "Input: FUNC_SEX(abcd), Output: abcFUNC_SEX(sss)xxxxxxx";
        Pattern pattern2 = Pattern.compile("FUNC_SEX[(?<=\\()][^)]*[(?=\\))]");
        Matcher matcher2 = pattern2.matcher(source2);
        while(matcher2.find()) {
            System.out.println(matcher2.group());//group() 默认就是 group(0)
        }

    }


}
