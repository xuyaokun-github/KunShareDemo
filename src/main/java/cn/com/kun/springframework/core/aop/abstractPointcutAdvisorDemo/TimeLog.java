package cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeLog {

    String name() default "";
}