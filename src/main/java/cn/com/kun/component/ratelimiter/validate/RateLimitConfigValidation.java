package cn.com.kun.component.ratelimiter.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 限流配置验证
 * author:xuyaokun_kzx
 * date:2021/7/1
 * desc:
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Constraint(validatedBy = {RateLimitConfigValidator.class})
public @interface RateLimitConfigValidation {

    /**
     * 下面三个参数是必须要加的，spring框架会校验
     * 假如没这三个属性，会直接抛异常，应用启动失败
     * @return
     */

    /**
     * 验证失败时，会输出这个字符串
     * @return
     */
    String message() default "限流配置有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
