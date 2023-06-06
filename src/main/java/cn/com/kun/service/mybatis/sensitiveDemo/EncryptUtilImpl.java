package cn.com.kun.service.mybatis.sensitiveDemo;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class EncryptUtilImpl implements IEncryptUtil {

    @Override
    public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
        //取出所有被EncryptTransaction注解的字段
        for (Field field : declaredFields) {
            EncryptDecryptField encryptDecryptField = field.getAnnotation(EncryptDecryptField.class);
            if (!Objects.isNull(encryptDecryptField)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                //暂时只实现String类型的加密
                if (object instanceof String) {
                    String value = (String) object;
                    //加密
                    try {
                        field.set(paramsObject, DBAESUtil.encrypt(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return paramsObject;
    }
}
