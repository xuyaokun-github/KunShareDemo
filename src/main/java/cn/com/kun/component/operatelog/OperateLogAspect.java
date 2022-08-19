package cn.com.kun.component.operatelog;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.spel.SpELHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 操作日志记录组件
 * 目前只完成了更新类型
 * 新增类型、删除类型、查询类型待补充
 *
 * author:xuyaokun_kzx
 * date:2022/7/5
 * desc:
*/
@Component
@Aspect
public class OperateLogAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(OperateLogAspect.class);

    @Autowired
    private SpELHelper spELHelper;

    @Pointcut("@annotation(cn.com.kun.component.operatelog.OperateLog)")
    public void pointCut(){

    }

    /**
     * 根据方法调用定义切点
     */
    @Pointcut("execution(* cn.com.kun.service.operatelog.OperatelogDemoService2.update(..)) || " +
            " execution(* cn.com.kun.service.operatelog.OperatelogDemoService3.update(..)) || " +
            " execution(* cn.com.kun.service.operatelog.OperatelogDemoService4.update4(..))")
    public void pointCut2(){

    }

    /**
     * 同时切两个入口
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "pointCut() || pointCut2()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        Object obj = pjp.proceed();

        try {
            //这里可以解析返回，假如判断是成功，再做记录，否则不做记录
            if (obj instanceof ResultVo){
                ResultVo resultVo = (ResultVo) obj;
                if (resultVo.isSuccess()){
                    //入参
                    Object[] args = pjp.getArgs();
                    OperateLogDO operateLogDO = new OperateLogDO();

                    // 获取方法上的OperateLog注解对象
                    OperateLog operateLog = method.getAnnotation(OperateLog.class);
                    if (operateLog != null){
                        //假如是用注解方式
                        String moduleName = operateLog.moduleName();
                        OperateTypeEnum operateTypeEnum = operateLog.operateType();
                        if (operateTypeEnum.equals(OperateTypeEnum.UPDATE)){
                            //获取更新前的内容(一般都是根据ID来查)
                            String oldContent = queryOldContent(operateLog, pjp);
                            LOGGER.info("内管操作成功，开始记录操作日志！当前模块：{} ", moduleName);
                            //save db
                            operateLogDO.setModuleName(moduleName);
                            //粗暴点，就全部记录，但是不够精简，假如希望精简，就用el表达式
                            String newContent = " 修改后内容：" + JacksonUtils.toJSONString(args);
                            operateLogDO.setOperDetail("修改前内容：" + oldContent + newContent);
                        }
                    }else {
                        //假如不用注解方式
                        LOGGER.info("记录操作日志，未使用注解方式，不做处理！");

                        //方法名，例如update
                        //通常来说，增删查改，一个方法就够了，不需要重载（可以按照规范，将它改成不是重载的方法）
                        String methodName = method.getName();
                        String declaringClassName = method.getDeclaringClass().getName();
                        //通过方法名和类名找到对应的 查询方法名和要调用的bean名 TODO
                        operateLogDO.setOperDetail("修改内容：" + JacksonUtils.toJSONString(args));

                    }
                    saveOperateLog(operateLogDO);

                }else {
                    //操作不成功，不做记录
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return obj;
    }

    private void saveOperateLog(OperateLogDO operateLogDO) {

        LOGGER.info("记录操作日志入库！内容：{} ", operateLogDO);
    }

    private String queryOldContent(OperateLog operateLog, JoinPoint joinPoint) {

        String targetName = operateLog.targetName();
        String methodName = operateLog.methodName();

        //SpEL表达式
        String spELExp = operateLog.selectKey();
        Object keyObj = spELHelper.generateKeyBySpEL(spELExp, joinPoint);

        LOGGER.info("SpEL表达式解析后得到内容：{}", keyObj);
        //然后调用方法
        //反射调用查询方法，拿到值，至于这个反射调用什么方法，见仁见智了

        //method名
        Object target = SpringContextUtil.getBean(targetName);
        Method method = ReflectionUtils.findMethod(target.getClass(), methodName, keyObj.getClass());
        Object res = ReflectionUtils.invokeMethod(method, target, keyObj);
        return JacksonUtils.toJSONString(res);
    }

}
