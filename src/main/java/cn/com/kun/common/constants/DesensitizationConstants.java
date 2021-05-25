package cn.com.kun.common.constants;

/**
 * 脱敏常量类
 */
public class DesensitizationConstants {

    /**
     * 身份证正则（其实可以考虑定义成枚举类）
     */
    public static final String ID_EXPRESSION = "(\\d{4})\\d{10}(\\w{4})";

    public static final String ID_REPLACE = "$1*****$2";
}
