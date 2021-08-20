package cn.com.kun.component.memorycache;

import java.lang.annotation.*;

/**
 * 清除内存缓存通知
 * 用了该注解的方法执行前要发出清除内存缓存的通知
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EvictCacheNotice {

    /**
     * 要清除的内存缓存管理器的名称
     * @return
     */
    String configName();

    /**
     * SpEl表达式
     * @return
     */
    String key() default "";

}
