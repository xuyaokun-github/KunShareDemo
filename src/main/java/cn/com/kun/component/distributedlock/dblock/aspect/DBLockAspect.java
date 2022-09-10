package cn.com.kun.component.distributedlock.dblock.aspect;

import cn.com.kun.component.distributedlock.dblock.DBLock;
import cn.com.kun.component.distributedlock.dblock.DistributedDbLock;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@Component
@Aspect
public class DBLockAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(DBLockAspect.class);

    @Autowired
    DistributedDbLock distributedDbLock;

    @Value("${dblock.clusterCode:}")
    private String dblockClusterCode;

    @Pointcut("@annotation(cn.com.kun.component.distributedlock.dblock.DBLock)")
    public void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        //获取方法上的DBClusterLock注解对象
        DBLock dbClusterLock = method.getAnnotation(DBLock.class);
        //锁资源
        String resourceName = dbClusterLock.resourceName();
        //是否拼接集群名称
        boolean withClusterCode = dbClusterLock.withClusterCode();
        if (withClusterCode){
            if (StringUtils.isNotEmpty(dblockClusterCode)){
                resourceName = dblockClusterCode + ":" + resourceName;
            }else {
                LOGGER.warn("数据库锁使用单集群模式时未定义集群代码，请检查dblock.clusterCode配置项");
            }
        }

        Object obj;
        try {
            distributedDbLock.lock(resourceName);
            //        if (!isLock){
//            LOGGER.error("DBLockAspect切面获取锁失败");
//            throw new RuntimeException("切面获取锁失败");
//        }
            obj = pjp.proceed();
        }finally {
            distributedDbLock.unlock(resourceName);
            //        if (!isUnLock){
//            LOGGER.error("DBLockAspect切面释放锁失败");
//            throw new RuntimeException("切面释放锁失败");
//        }
        }

        return obj;
    }


}
