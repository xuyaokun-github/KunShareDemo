package cn.com.kun.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏注解
 * author:xuyaokun_kzx
 * date:2021/5/25
 * desc:
*/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationField {

    /**
     * 脱敏正则表达式
     * @return
     */
    String expression() default "([\\S]+)";

    /**
     * 要替换的目标内容，默认是5个星号
     * @return
     */
    String replace() default "*****";


}
