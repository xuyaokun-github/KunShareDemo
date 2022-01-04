package cn.com.kun.springframework.core.jackson.lanyang;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义注解
 * 放到需要转换的属性上
 * author:xuyaokun_kzx
 * date:2021/12/16
 * desc:
*/
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = CustomStatusSerialize.class)
public @interface StatusWrapped {

}
