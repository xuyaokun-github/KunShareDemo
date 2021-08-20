package cn.com.kun.component.ratelimiter;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimit {

    /**
     * 业务场景名
     * @return
     */
    String bizSceneName() default "";

    /**
     * SpEl表达式
     * 解析得到子场景名
     * @return
     */
    String key() default "";

    /**
     * 限流模式：
     * forward:向前限流
     * 注解加在控制层的方法上
     *
     * backward:向后限流
     * 默认是backward
     * 注解加在具体业务层的方法上
     *
     * @return
     */
    String mode() default "backward";

}
