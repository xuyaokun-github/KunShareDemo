package cn.com.kun.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Aspect//假如了这个注解，就相当于定义一个切面类，但是前提是该类也被定义为bean
public class MyAspect {

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
        System.out.println("i am before........");
    }

    /*
     * 使用这种带参数的方式
     * 切入点的方法的参数就会被赋到这个方法的参数上（参数名可以不同，随意）
     */
    @Before("pointcut() && args(arg)")
    public void beforeWithParam(String arg) {
        System.out.println("BeforeWithParam." + arg);
    }

    //注意点：假如使用这些通知注解，括号中假如不指定切入点，那么加载配置时就会报错
    /*
     * 定义一个返回通知
     */
    @AfterReturning("pointcut()")
    public void afterReturningMethod(){
        System.out.println("AfterReturning............");
    }
    /*
     * 定义一个异常通知
     */
    @AfterThrowing(pointcut="pointcut()", throwing="e")
    public void afterThrowing(RuntimeException e) {
        System.out.println("AfterThrowing : " + e.getMessage());
    }
    /*
     * 定义一个后置通知
     */
    @After("pointcut()") //传入的是切入点的方法名
    public void after() {
        System.out.println("After.");
    }
    /*
     * 定义一个环绕通知
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Around 1.");
        Object obj = pjp.proceed();
        System.out.println("Around 2.");
        System.out.println("Around : " + obj);
        return obj;
    }

}
