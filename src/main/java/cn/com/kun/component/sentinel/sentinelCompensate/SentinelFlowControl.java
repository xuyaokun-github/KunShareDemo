package cn.com.kun.component.sentinel.sentinelCompensate;

import java.lang.annotation.*;

/**
 * sentinel扩展--限流补偿
 * author:xuyaokun_kzx
 * date:2022/5/18
 * desc:
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SentinelFlowControl {

    /**
     * 限流资源名称
     * @return
     */
    String value();

    /**
     * 业务场景名
     * 假如为空，就取value()的值
     * @return
     */
    String bizName() default "";

    /**
     * 停顿时间，默认是50ms
     * @return
     */
    long pauseTime() default 50;

}
