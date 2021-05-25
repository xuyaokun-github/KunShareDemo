package cn.com.kun.common.constants;

/**
 * 脱敏常量类
 */
public class DesensitizationConstants {

    /**
     * 身份证正则（其实可以考虑定义成枚举类）
     * 只保留前四位和后四位
     */
    public static final String ID_EXPRESSION = "(\\d{4})\\d{10}(\\w{4})";

    public static final String ID_REPLACE = "$1*****$2";

    /**
     * 邮编脱敏
     * 保留前五位，脱敏中间五位，保留后面若干位
     */
    public static final String POSTCODE_EXPRESSION = "(\\d{5})\\d{5}(\\w+)";

    public static final String POSTCODE_REPLACE = "$1*****$2";

    /**
     * 街道名脱敏
     * 只保留前面三位
     */
    public static final String STREETNAME_EXPRESSION = "(.{3})(.*)(.{0})";

    public static final String STREETNAME_REPLACE = "$1**$3";

    /**
     * 邮箱脱敏
     */
    public static final String EMAIL_EXPRESSION = "(\\w+)\\w{5}@(\\w+)";

    public static final String EMAIL_REPLACE = "$1***@$2";

}


