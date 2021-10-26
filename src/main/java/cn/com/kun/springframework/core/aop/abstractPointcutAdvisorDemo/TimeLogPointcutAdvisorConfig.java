package cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AbstractPointcutAdvisor的使用例子
 * org.springframework.aop.support.AbstractPointcutAdvisor
 *
 * 通过AbstractPointcutAdvisor定义切面（切入点+通知）
 *
 * author:xuyaokun_kzx
 * date:2021/10/26
 * desc:
*/
@Configuration
public class TimeLogPointcutAdvisorConfig extends AbstractPointcutAdvisor implements IntroductionAdvisor, BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 定义通知
     */
    private Advice advice;

    /**
     * 定义切入点
     */
    private Pointcut pointcut;

    @PostConstruct
    public void init() {
        Set<Class<? extends Annotation>> retryableAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>(1);
        retryableAnnotationTypes.add(TimeLog.class);
        //构建切入点
        this.pointcut = buildPointcut(retryableAnnotationTypes);
        //构建通知
        this.advice = buildAdvice();
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Advice buildAdvice() {
        //通知实现类，实现了IntroductionInterceptor接口
        TimeLogIntroductionInterceptor interceptor = new TimeLogIntroductionInterceptor();
        return interceptor;
    }

    private Pointcut buildPointcut(Set<Class<? extends Annotation>> retryAnnotationTypes) {
        //切入点是可以组合的，可以组合多个切入点
        ComposablePointcut result = null;
        for (Class<? extends Annotation> retryAnnotationType : retryAnnotationTypes) {
            Pointcut filter = new AnnotationClassOrMethodPointcut(retryAnnotationType);
            if (result == null) {
                result = new ComposablePointcut(filter);
            } else {
                //多个切入点组合
                result.union(filter);
            }
        }
        return result;
    }

    /**
     * 本类中的
     * AnnotationClassOrMethodPointcut、AnnotationClassOrMethodFilter、AnnotationMethodsResolver
     * 是可以复用的，主要负责的就是方法的匹配
     */
    private final class AnnotationClassOrMethodPointcut extends StaticMethodMatcherPointcut {

        private final MethodMatcher methodResolver;

        AnnotationClassOrMethodPointcut(Class<? extends Annotation> annotationType) {
            this.methodResolver = new AnnotationMethodMatcher(annotationType);
            //类过期器
            setClassFilter(new AnnotationClassOrMethodFilter(annotationType));
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return getClassFilter().matches(targetClass) || this.methodResolver.matches(method, targetClass);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AnnotationClassOrMethodPointcut)) {
                return false;
            }
            AnnotationClassOrMethodPointcut otherAdvisor = (AnnotationClassOrMethodPointcut) other;
            return ObjectUtils.nullSafeEquals(this.methodResolver, otherAdvisor.methodResolver);
        }

    }

    private final class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

        private final AnnotationMethodsResolver methodResolver;

        AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType) {
            super(annotationType, true);
            this.methodResolver = new AnnotationMethodsResolver(annotationType);
        }

        @Override
        public boolean matches(Class<?> clazz) {
            return super.matches(clazz) || this.methodResolver.hasAnnotatedMethods(clazz);
        }

    }

    /**
     * 方法解析器
     * 判断是否存在该注解类型的方法
     */
    private static class AnnotationMethodsResolver {

        private Class<? extends Annotation> annotationType;

        public AnnotationMethodsResolver(Class<? extends Annotation> annotationType) {
            this.annotationType = annotationType;
        }

        public boolean hasAnnotatedMethods(Class<?> clazz) {
            final AtomicBoolean found = new AtomicBoolean(false);
            ReflectionUtils.doWithMethods(clazz,
                    new ReflectionUtils.MethodCallback() {
                        @Override
                        public void doWith(Method method) throws IllegalArgumentException,
                                IllegalAccessException {
                            if (found.get()) {
                                return;
                            }
                            Annotation annotation = AnnotationUtils.findAnnotation(method,
                                    annotationType);
                            if (annotation != null) { found.set(true); }
                        }
                    });
            return found.get();
        }

    }


    @Override
    public ClassFilter getClassFilter() {
        return pointcut.getClassFilter();
    }

    @Override
    public void validateInterfaces() throws IllegalArgumentException {

    }

    @Override
    public Class<?>[] getInterfaces() {
        return new Class[] { TimeLog.class };
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
