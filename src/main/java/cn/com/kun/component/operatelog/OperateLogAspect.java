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

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        Object obj = pjp.proceed();
        //这里可以解析返回，假如判断是成功，再做记录，否则不做记录
        if (obj instanceof ResultVo){
            ResultVo resultVo = (ResultVo) obj;
            if (resultVo.isSuccess()){

                //入参
                Object[] args = pjp.getArgs();
                // 获取方法上的OperateLog注解对象
                OperateLog operateLog = method.getAnnotation(OperateLog.class);
                //锁资源
                String moduleName = operateLog.moduleName();
                OperateTypeEnum operateTypeEnum = operateLog.operateType();

                OperateLogDO operateLogDO = new OperateLogDO();
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
                LOGGER.info("记录操作日志入库！内容：{} ", operateLogDO);

            }else {
                //操作不成功，不做记录
            }

        }

        return obj;
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
