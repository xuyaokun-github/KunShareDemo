package cn.com.kun.springframework.core.jackson.sensitivedemo;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface SensitiveWrapped {


    /**
     * 脱敏类型
     * (注解和枚举类一起使用)
     * @return
     */
    SensitiveEnum value();
}
