package cn.com.kun.component.spel;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 负责SpEl表达式的处理
 *
 * author:xuyaokun_kzx
 * date:2021/7/2
 * desc:
*/
@Component
public class SpELHelper {

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();


    /**
     * 根据SpEL表达式得到具体的值
     * ProceedingJoinPoint和 JoinPoint 都支持
     *
     *  根据SpEL表达式从上下文中计算出实际参数值
     *   如:
     *    @annotation(key="#student.name")
     *    public void method(Student student)
     *   可以解析出方法形参的某属性值，
     *   return "kunghsu";
     *
     * @param spELString
     * @param joinPoint
     * @return
     */
    public Object generateKeyBySpEL(String spELString, JoinPoint joinPoint) {
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用spring的DefaultParameterNameDiscoverer获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        // 解析过后的Spring表达式对象
        Expression expression = parser.parseExpression(spELString);
        // spring的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        // 通过joinPoint获取被注解方法的形参
        Object[] args = joinPoint.getArgs();
        // 给上下文赋值
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        return expression.getValue(context);
    }

}
