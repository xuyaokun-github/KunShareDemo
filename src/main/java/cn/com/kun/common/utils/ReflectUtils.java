package cn.com.kun.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectUtils {

    /**
     * 通过反射获取类上的某个属性值(不包括父类)
     * @param sourceObj
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static Object getValue(Object sourceObj, String fieldName) throws IllegalAccessException, ClassNotFoundException {
        Class ownerClass2 = Class.forName(sourceObj.getClass().getName());
        Field[] fields = ownerClass2.getDeclaredFields();
        Object obj = null;
        for(Field field : fields){
            if(field.getName().equals(fieldName)){
                //设为可访问
                field.setAccessible(true);
                obj =  field.get(sourceObj);
            }
        }
        return obj;
    }

    /**
     * 获取所有属性（包括父类的）
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 根据属性名获取具体的值（包含父类）
     * @param obj
     * @param data
     * @return
     * @throws IllegalAccessException
     */
    public static Object getValueIncludeParent(Object obj, String data) throws IllegalAccessException {
        Object obj2 = null;
        Field[] fields = ReflectUtils.getAllFields(obj);
        for(Field field : fields){
            if(field.getName().equals("data")){
                //设为可访问
                field.setAccessible(true);
                obj2 =  field.get(obj);
            }
        }
        return obj2;
    }

}
