package cn.com.kun.component.aspect;

import cn.com.kun.common.advice.SecretResponseBodyAdvice;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

//@Component
//@Aspect//假如了这个注解，就相当于定义一个切面类，但是前提是该类也被定义为bean
public class MyAspect {

    public final static Logger logger = LoggerFactory.getLogger(SecretResponseBodyAdvice.class);

    @Autowired
    private DataSource druidDataSource;

    /**
     * 定义一个切入点 例子：cn.com.kun.controller这个包下的所有类的所有方法都会被切入
     */
    @Pointcut("execution(* cn.com.kun.controller.*.*(..))")
    public void pointcut() {}
    /**
     * 定义一个切入点 例子：同理 cn.com.kun.controller这个包下的所有类的所有方法都会被切入
     */
    @Pointcut("within(cn.com.kun.controller.*)")
    public void bizPointcut() {}

    /**
     * 定义一个前置通知，在定义时指定使用哪个切入点
     */
    @Before("execution(* cn.com.kun.controller.*.*(..))")
//	@Before("pointcut()") //也可以使用这种方式，上面已经定义过切入点，可以直接拿来用
    public void beforeMethod(){
        logger.info("i am before........");
    }

    /*
     * 使用这种带参数的方式
     * 切入点的方法的参数就会被赋到这个方法的参数上（参数名可以不同，随意）
     */
    @Before("pointcut() && args(arg)")
    public void beforeWithParam(String arg) {
        logger.info("BeforeWithParam." + arg);
    }

    //注意点：假如使用这些通知注解，括号中假如不指定切入点，那么加载配置时就会报错
    /*
     * 定义一个返回通知
     */
    @AfterReturning("pointcut()")
    public void afterReturningMethod(){
        logger.info("AfterReturning............");
    }
    /*
     * 定义一个异常通知
     */
    @AfterThrowing(pointcut="pointcut()", throwing="e")
    public void afterThrowing(RuntimeException e) {
        logger.info("AfterThrowing : " + e.getMessage());
    }
    /*
     * 定义一个后置通知
     */
    @After("pointcut()") //传入的是切入点的方法名
    public void after() {
        logger.info("After.");
    }
    /*
     * 定义一个环绕通知
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("Around 1.");
        Object obj = pjp.proceed();
        logger.info("Around 2.");
        logger.info("Around : " + obj);
        return obj;
    }

}
