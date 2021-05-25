package cn.com.kun.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏注解
 * （其实可以考虑将加解密和脱敏类的注解合并成一个注解）
 *
 * author:xuyaokun_kzx
 * date:2021/5/24
 * desc:
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationAnnotation {

}
