package cn.com.kun.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加解密注解
 * 用在属性上
 * author:xuyaokun_kzx
 * date:2021/5/24
 * desc:
*/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecretField {

    /**
     * 加密
     * @return
     */
    boolean encode() default false;

    /**
     * 是否需要解密
     * @return
     */
    boolean decode() default false;

}
