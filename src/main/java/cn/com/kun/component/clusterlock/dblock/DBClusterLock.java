package cn.com.kun.component.clusterlock.dblock;

import java.lang.annotation.*;

/**
 * 数据库分布式锁
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
 * TODO:实现悲观乐观模式切换
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DBClusterLock {

    /**
     * 锁资源名称：
     * 一般可用类名拼方法名做资源名称（假如方法有重载需要加上参数名做进一步区分），
     * 也可定义更具体的名称
     * 一个名称对应一把锁
     * @return
     */
    String resourceName();

}
