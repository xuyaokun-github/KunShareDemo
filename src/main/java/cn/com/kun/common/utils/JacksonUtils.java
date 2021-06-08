package cn.com.kun.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        //如果存在未知属性，则忽略不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //允许key没有双引号
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许key有单引号
//        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //允许整数以0开头
//        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        //允许字符串中存在回车换行控制符
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
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
