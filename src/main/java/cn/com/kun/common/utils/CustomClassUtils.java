package cn.com.kun.common.utils;

import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.Map;

public class CustomClassUtils {

    /**
     * 是否基础数据类型或者包装类型
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveOrWrapper(Class clazz){
        return ClassUtils.isPrimitiveOrWrapper(clazz);
    }

    /**
     * 是否为数组类型
     * @param clazz
     * @return
     */
    public static boolean isArrayOrCollection(Class clazz){
        //判断返回类型是否是集合类型
        boolean isCollection = Collection.class.isAssignableFrom(clazz);
        //判断返回类型是否是数组类型
        boolean isArray = clazz.isArray();
        return isCollection || isArray;
    }

    /**
     * 是否为数组类型
     * @param clazz
     * @return
     */
    public static boolean isArray(Class clazz){
        //判断返回类型是否是数组类型
        boolean isArray = clazz.isArray();
        return isArray;
    }

    /**
     * 是否为集合类型
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class clazz){
        //判断返回类型是否是集合类型
        boolean isCollection = Collection.class.isAssignableFrom(clazz);
        return isCollection;
    }

    /**
     * 是否为map的子类
     * @param object
     * @return
     */
    public static boolean isMap(Object object){
        return object instanceof Map;
    }

}
