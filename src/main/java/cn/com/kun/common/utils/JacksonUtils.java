package cn.com.kun.common.utils;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * author:xuyaokun_kzx
 * date:2020/10/19
 * desc:
 */
public class JacksonUtils {

    private static final ObjectMapper mapper;
    private final static Logger log = LoggerFactory.getLogger(JacksonUtils.class);

    /**
     * 设置通用的属性
     */
    static {
        //com.fasterxml.jackson.databind.ObjectMapper
        mapper = new ObjectMapper();
        //如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        //反序列化时，如果存在未知属性，则忽略不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //允许key没有双引号
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许key有单引号
//        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //允许整数以0开头
//        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        //允许字符串中存在回车换行控制符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        //全局设置：属性为NULL 不序列化
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    /**
     * 对象->字符串 （不格式化）
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj) {
        return obj != null ? toJSONString(obj, () -> "", false) : "";
    }

    /**
     * 对象->字符串 （格式化）
     * @param obj
     * @return
     */
    public static String toFormatJSONString(Object obj) {
        return obj != null ? toJSONString(obj, () -> "", true) : "";
    }


    public static String toJSONString(Object obj, Supplier<String> defaultSupplier, boolean format) {
        try {
            if (format) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (Throwable e) {
            log.error(String.format("toJSONString %s", obj != null ? obj.toString() : "null"), e);
        }
        return defaultSupplier.get();
    }

    /**
     * 对象->map对象
     * @param value
     * @return
     */
    public static Map<String, Object> toJSON(Object value) {
        return value != null ? toMap(value, () -> null) : null;
    }

    /**
     * 字符串转成Map对象（对应原Fastjson的字符串转JSONObject）
     * @param value
     * @return
     */
    public static Map<String, Object> parseObject(String value) {
        return value != null ? toMap(value, () -> null) : null;
    }

    /**
     * 字符串转成Java对象
     *
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String value, Class<T> tClass) {
        return toJavaObject(value, tClass);
    }

    public static <T> T toJavaObject(String value, Class<T> tClass) {
        return StringUtils.isNotBlank(value) ? toJavaObject(value, tClass, () -> null) : null;
    }

    /**
     * 对象转成Java对象
     * @param obj (可以是阿里的JSONObject对象或者是Map对象)
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T toJavaObject(Object obj, Class<T> tClass) {
        //先将obj转成字符串
        return obj != null ? toJavaObject(toJSONString(obj), tClass, () -> null) : null;
    }

    public static <T> T toJavaObject(String value, Class<T> tClass, Supplier<T> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            //字符串转为Java对象
            return mapper.readValue(value, tClass);
        } catch (Throwable e) {
            log.error(String.format("toJavaObject exception: \n %s\n %s", value, tClass), e);
        }
        return defaultSupplier.get();
    }

    /**
     * 根据字符串得到具体的Java对象，支持泛型
     * @param obj
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T toJavaObject(String obj, TypeReference valueTypeRef) {
        //先将obj转成字符串
        return obj != null ? toJavaObject(obj, valueTypeRef, () -> null) : null;
    }

    /**
     * 支持泛型
     * @param value
     * @param valueTypeRef
     * @param defaultSupplier
     * @param <T>
     * @return
     */
    public static <T> T toJavaObject(String value, TypeReference valueTypeRef, Supplier<T> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            //字符串转为Java对象
            return mapper.readValue(value, valueTypeRef);
        } catch (Throwable e) {
            log.error(String.format("toJavaObject exception: \n %s\n %s", value, valueTypeRef), e);
        }
        return defaultSupplier.get();
    }

    /**
     * 字符串转数组（Object默认是LinkedHashMap）
     * @param value
     * @return
     */
    public static List<Object> parseArray(String value) {
        return StringUtils.isNotBlank(value) ? toList(value, () -> null) : null;
    }

    /**
     * 符串转Java对象数组
     *
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String value, Class<T> tClass) {
        return toJavaObjectList(value, tClass);
    }

    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass) {
        return StringUtils.isNotBlank(value) ? toJavaObjectList(value, tClass, () -> null) : null;
    }

    public static <T> List<T> toJavaObjectList(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObjectList(toJSONString(obj), tClass, () -> null) : null;
    }

    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass, Supplier<List<T>> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, tClass);
            return mapper.readValue(value, javaType);
        } catch (Throwable e) {
            log.error(String.format("toJavaObjectList exception \n%s\n%s", value, tClass), e);
        }
        return defaultSupplier.get();
    }

    // 简单地直接用json复制或者转换(Cloneable)
    public static <T> T jsonCopy(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObject(toJSONString(obj), tClass) : null;
    }

    public static Map<String, Object> toMap(String value) {
        return StringUtils.isNotBlank(value) ? toMap(value, () -> null) : null;
    }

    /**
     * 支持value中有多余的未转义的双引号（简而言之，支持非法的json串）
     * @param value
     * @return
     */
    public static Map<String, Object> toMapSupportSpecialChar(String value) {

        Map<String, Object> res = null;
        String source = value;
        int loopCount = 1;//非法字符出现的次数，默认支持最多100次，超过100次不做处理
        while (true){
            try {
                //字符串转为Java对象
                return mapper.readValue(source, LinkedHashMap.class);
            } catch (Throwable e) {
                if (e instanceof com.fasterxml.jackson.core.JsonParseException){
                    String errorMsg = e.getMessage();
                    //有些时候，堆栈不一定含有关键字code 34
//                    boolean isDoubleQuotesParseException = errorMsg.contains("(code 34)): was expecting comma to separate Object entries");
                    boolean isDoubleQuotesParseException = errorMsg.contains("was expecting comma to separate Object entries");
                    if (isDoubleQuotesParseException){
                        //假如是出现在第一行，才做处理，有时候双引号可能会出现在第二行，这样的处理就会比较复杂，先不考虑这种情况
                        log.warn("出现双引号解析异常，进行替换处理,源串：{}", value);
                        //替换有问题的双引号（多余的双引号，改成单引号）
                        source = replaceForDoubleQuotes(source);
                        //这里有可能陷入死循环，设置一个固定的循环次数，到次数不再continue
                        if (loopCount-- > 0){
                            continue;
                        }
                    } if (isBackslashParseException(e)) {
//                        e.printStackTrace();//调试时用
                        log.warn("出现反斜杠解析异常，进行替换处理,源串：{}", value);
                        //反斜杠替换处理
                        source = replaceForBackslash(e, source);
                        if (loopCount-- > 0){
                            continue;
                        }
                    }else {
                        //出现无法处理的异常
                        log.warn("出现无法处理的json解析异常，错误信息：{}", errorMsg);
                    }
                }else {
                    log.warn("json反序列化异常", e);
                }
            }
            break;
        }

        return res;
    }

    /**
     * 反斜杆处理
     * 目的是支持反斜杠（将一个反斜杠替换成两个反斜杠，反序列化后即可保留该反斜杠）
     *
     * @param e
     * @param source
     * @return
     */
    private static String replaceForBackslash(Throwable e, String source) {

        String newSource = source;
        try {
            JsonProcessingException jsonProcessingException = (JsonProcessingException)e;
            JsonLocation jsonlocation = jsonProcessingException.getLocation();
            String sourceRef = (String) jsonlocation.getSourceRef();
            if (StringUtils.isNotEmpty(sourceRef)){
                String[] strings = sourceRef.split("\n");
                //问题行
                String targetLine = strings[jsonlocation.getLineNr() - 1];
                //补多一个\到问题行
                String newTargetLine = targetLine.replace("\\", "\\\\");
                strings[jsonlocation.getLineNr() - 1] = newTargetLine;
                newSource = source.replace(targetLine, newTargetLine);
            }
        }catch (Exception exception){
            log.warn("replaceForBackslash函数异常", exception);
        }
        return newSource;
    }

    /**
     * 处理多余反斜杠导致的异常
     *
     * @param throwable
     * @return
     */
    private static boolean isBackslashParseException(Throwable throwable) {

        if (!(throwable instanceof JsonProcessingException)){
            return false;
        }
        try {
            //无法识别的字符转义
            boolean isParseException = throwable.getMessage().contains("Unrecognized character escape");

            boolean isExistBackslash = false;
            JsonProcessingException jsonProcessingException = (JsonProcessingException)throwable;
            JsonLocation jsonlocation = jsonProcessingException.getLocation();
            String sourceRef = (String) jsonlocation.getSourceRef();
            if (StringUtils.isNotEmpty(sourceRef)){
                String[] strings = sourceRef.split("\n");
                String targetLine = strings[jsonlocation.getLineNr() - 1];
                int backslashIndex = targetLine.indexOf('\\');
                if (backslashIndex > 0){
                    //存在反斜杠
                    isExistBackslash = true;
                }
            }

            return isParseException && isExistBackslash;
        }catch (Exception e){
            log.warn("isBackslashParseException函数异常", e);
        }
        return false;
    }


    private static String replaceForDoubleQuotes(String source) {

        char[] charArr = source.toCharArray();
        List<Integer> extraDoubleQuotesIndexList = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < charArr.length; i++) {
            if (Integer.valueOf(charArr[i]).equals(Integer.valueOf((":".charAt(0))))){
                //识别到:
                //只识别value部分（默认key部分是合法的，所以不处理key部分）
                indexList = new ArrayList<>();
            }
            if (Integer.valueOf(charArr[i]) == 34){
                //识别到双引号
                indexList.add(i);
            }
            if (Integer.valueOf(charArr[i]).equals(Integer.valueOf(",".charAt(0)))
                || Integer.valueOf(charArr[i]).equals(Integer.valueOf("}".charAt(0)))){
                //开始校验indexList，是不是大于2
                //大于2的，都是问题字符
                if (indexList.size() > 2){
                    //保留第一个和最后一个，其他的多余的双引号
                    indexList.remove(0);
                    indexList.remove(indexList.size()-1);
                    extraDoubleQuotesIndexList.addAll(indexList);
                }else {
                    //修复bug:遇到} 应该重新计数，有一类报文，可能在value里又是一个独立的json字符串
                    //例如：{"a1":"v1","a3":"v3kkkk"888888"","a2":"v2","url":"ctrl://ffffffff{\"aaa\":\"dddd\"}"}
                    indexList = new ArrayList<>();
                }
            }

        }

        //替换双引号字符，改成单引号
        if (extraDoubleQuotesIndexList.size() > 0){
            StringBuilder builder = new StringBuilder(source);
            for (Integer index : extraDoubleQuotesIndexList){
                builder.setCharAt(index, "'".charAt(0));
            }
            return builder.toString();
        }

        return source;
    }


    public static Map<String, Object> toMap(Object value) {
        return value != null ? toMap(value, () -> null) : null;
    }

    public static Map<String, Object> toMap(Object value, Supplier<Map<String, Object>> defaultSupplier) {
        if (value == null) {
            return defaultSupplier.get();
        }
        try {
            if (value instanceof Map) {
                //假如已经是map类型，不需要处理了，直接返回即可
                return (Map<String, Object>) value;
            }
        } catch (Exception e) {
            log.info("fail to convert" + toJSONString(value), e);
        }
        return toMap(toJSONString(value), defaultSupplier);
    }

    public static Map<String, Object> toMap(String value, Supplier<Map<String, Object>> defaultSupplier) {
        if (StringUtils.isBlank(value)) {
            return defaultSupplier.get();
        }
        try {
            //根据字符串，转成LinkedHashMap对象
            return toJavaObject(value, LinkedHashMap.class);
        } catch (Exception e) {
            log.error(String.format("toMap exception\n%s", value), e);
        }
        return defaultSupplier.get();
    }


    public static List<Object> toList(String value) {
        return StringUtils.isNotBlank(value) ? toList(value, () -> null) : null;
    }

    public static List<Object> toList(Object value) {
        return value != null ? toList(value, () -> null) : null;
    }

    public static List<Object> toList(String value, Supplier<List<Object>> defaultSuppler) {
        if (StringUtils.isBlank(value)) {
            return defaultSuppler.get();
        }
        try {
            return toJavaObject(value, List.class);
        } catch (Exception e) {
            log.error("toList exception\n" + value, e);
        }
        return defaultSuppler.get();
    }

    public static List<Object> toList(Object value, Supplier<List<Object>> defaultSuppler) {
        if (value == null) {
            return defaultSuppler.get();
        }
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return toList(toJSONString(value), defaultSuppler);
    }

}
