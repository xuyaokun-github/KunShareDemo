package cn.com.kun.component.ratelimiter;

import java.lang.annotation.*;

/**
 * 限流
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
     * 要清除的内存缓存管理器的名称
     * @return
     */
    String bizSceneName() default "";

    /**
     * SpEl表达式
     * @return
     */
    String key() default "";

    /**
     * 限流模式：
     * forward:向前限流
     * backward:向后限流
     * 默认是backward
     * @return
     */
    String mode() default "backward";

    /**
     * 向前限流时使用，指定用哪一个控制层的限流配置
     * @return
     */
    String controllerName() default "";

}
