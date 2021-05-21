package cn.com.kun.schedule.xxljob.annotation;

import java.lang.annotation.*;

/**
 * 为了修复xxl-job2.1.2版本不支持@Bean+@StepScope定义bean的问题
 * 用了@XxlJob注解的类，必须要在方法上加@XxlJobClass注解
 * 和CustomXxlJobSpringExecutor配合使用
 *
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlJobClass {

    String value() default "";

}
