package cn.com.kun.component.clusterlock.dblock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@Component
@Aspect
public class DBClusterLockAspect {

    public final static Logger LOGGER = LoggerFactory.getLogger(DBClusterLockAspect.class);

    @Autowired
    DBClusterLockHandler dbClusterLockHandler;

    @Pointcut("@annotation(cn.com.kun.component.clusterlock.dblock.DBClusterLock)")
    public void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // 获取方法上的EvictCacheNotice注解对象
        DBClusterLock dbClusterLock = method.getAnnotation(DBClusterLock.class);
        //锁资源
        String resourceName = dbClusterLock.resourceName();
        boolean isLock = dbClusterLockHandler.lockPessimistic(resourceName);
        if (!isLock){
            throw new RuntimeException("切面获取锁失败");
        }
        Object obj = pjp.proceed();
        boolean isUnLock = dbClusterLockHandler.unlockPessimistic(resourceName);
        if (!isUnLock){
            throw new RuntimeException("切面释放锁失败");
        }
        return obj;
    }


}
