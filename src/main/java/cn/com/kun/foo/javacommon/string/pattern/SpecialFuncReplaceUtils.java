package cn.com.kun.foo.javacommon.string.pattern;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板特殊函数替换工具类
 * author:xuyaokun_kzx
 * date:2022/12/7
 * desc:
*/
public class SpecialFuncReplaceUtils {

    //
    private static Set<String> funcSet = new HashSet<String>(){{
        add("FUNC_SEX");
        add("FUNC_SEX2");
    }};

    public static void main(String[] args) {

        System.out.println(findKey("kunghsu(ddddde)ooooo"));

        //上游入参
        Map<String, String> data = new HashMap<>();
        data.put("sex1", "0");
        data.put("sex2", "1");

        //模板内容
        String templateContent = "Input: FUNC_SEX(sex1), Output: abcFUNC_SEX(sex2)xxxxxxx";
        String newTemplateContent = SpecialFuncReplaceUtils.replace(templateContent, data);
        System.out.println(String.format("old:%s \nnew:%s", templateContent, newTemplateContent));
    }

    private static String replace(String templateContent, Map<String, String> data) {

        //
        if (isExistFunc(templateContent)){
            //需要做特殊函数替换
            String res = templateContent;
            for (String func : funcSet){
                res = replaceForFunc(res, func, data);
            }
            return res;
        }
        return templateContent;
    }

    /**
     *
     * @param res 待替换内容
     * @param func 函数名
     * @param data 入参
     * @return
     */
    private static String replaceForFunc(String res, String func, Map<String, String> data) {

        Pattern pattern2 = Pattern.compile(func + "[(?<=\\()][^)]*[(?=\\))]");
        Matcher matcher2 = pattern2.matcher(res);
        String newString = res;
        while(matcher2.find()) {
            String matchString = matcher2.group();
            String key = findKey(matchString);
            newString = newString.replace(matchString, applyFunc(getValue(key, data)));
        }
        return newString;
    }

    private static String applyFunc(String value) {

        //这里运用策略模式，根据函数名匹配到具体的函数，然后得到具体的值(可以用map注册不同的函数逻辑)
        //TODO
        return value;
    }


    private static String getValue(String key, Map<String, String> data) {
        return data.containsKey(key)? (StringUtils.isEmpty(data.get(key))? "" : data.get(key)) : "";
    }

    private static String findKey(String matchString) {

        //找到第一个(的索引和第一个)的索引
        return matchString.substring(matchString.indexOf("(") + 1, matchString.indexOf(")")).trim();
    }

    private static boolean isExistFunc(String templateContent) {

        for (String func : funcSet){
            if (templateContent.contains(func)){
                return true;
            }
        }
        return false;
    }

}
