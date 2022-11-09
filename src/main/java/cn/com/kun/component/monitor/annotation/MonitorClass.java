package cn.com.kun.component.monitor.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

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
public @interface MonitorClass {

    String value() default "";

    String bizType() default "";

    /**
     * 配合TimeUnit一起用
     */
    long timePeriod() default 10;

    /**
     * 默认情况为 1秒一次
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
