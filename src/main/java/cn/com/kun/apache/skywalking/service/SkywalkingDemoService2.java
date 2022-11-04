package cn.com.kun.apache.skywalking.service;

import akka.dispatch.forkjoin.ThreadLocalRandom;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 实现黑名单的功能-用什么数据结构，视需求决定
 *
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
@Service
public class SkywalkingDemoService2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(SkywalkingDemoService2.class);


    @Autowired
    private RedisTemplate redisTemplate;

    static {

        //cn.com.kun.apache.skywalking.service.SkywalkingDemoService2.method4
        //获取类池
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = null;
        try {
            //获取类
            ctClass = classPool.getCtClass("cn.com.kun.apache.skywalking.service.SkywalkingDemoService2");
            //获取方法
            CtMethod method = ctClass.getDeclaredMethod("method4");

            ClassFile classFile = ctClass.getClassFile();
            ConstPool constPool = classFile.getConstPool();

            // 添加方法注解
            AnnotationsAttribute methodAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation methodAnnot = new Annotation(Trace.class.getName(), constPool);
            methodAttr.addAnnotation(methodAnnot);
            method.getMethodInfo().addAttribute(methodAttr);

            /*
            将修改后的CtClass加载至当前线程的上下文类加载器中，CtClass的toClass方法是通过调用本方法实现。
            需要注意的是一旦调用该方法，则无法继续修改已经被加载的class；
            通过类加载器加载该CtClass。
             */
//            ctClass.toClass();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void method1() throws InterruptedException {

        //模拟一个耗时
        Thread.sleep(300);
        //访问redis
        redisTemplate.opsForValue().get("kunghsu");
        //访问数据库

    }

    /**
     * 最简单的用法
     *
     * @throws InterruptedException
     */
    @Trace
    public void method2() throws InterruptedException {

        //模拟一个随机耗时
        Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
    }

    @Trace(operationName = "SkywalkingDemoService2.method3")
    public void method3() throws InterruptedException {

        //模拟一个随机耗时
        Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
    }

    /**
     * 用字节码技术动态加注解(采集不奏效)
     *
     */
    public void method4() throws InterruptedException {

        Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
    }


}
