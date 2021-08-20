package cn.com.kun.component.hystrixextend;

import java.lang.annotation.*;

/**
 * 基于Hystrix限流功能的扩展
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HystrixRateLimitExtend {

    /**
     * 业务场景名称
     * @return
     */
    String bizSceneName() default "";

    /**
     * SpEl表达式
     * @return
     */
    String key() default "";
}
