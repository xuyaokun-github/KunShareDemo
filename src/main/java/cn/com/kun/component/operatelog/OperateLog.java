package cn.com.kun.component.operatelog;

import java.lang.annotation.*;

/**
 * 记录操作日志
 * author:xuyaokun_kzx
 * date:2022/7/5
 * desc:
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OperateLog {

    /**
     * 模块名称
     */
    String moduleName();

    /**
     * 操作类型
     * @return
     */
    OperateTypeEnum operateType();

    /**
     * 查询方法的入参对应的SpEL表达式
     * @return
     */
    String selectKey() default "";

    String targetName() default "";

    String methodName() default "";

}
