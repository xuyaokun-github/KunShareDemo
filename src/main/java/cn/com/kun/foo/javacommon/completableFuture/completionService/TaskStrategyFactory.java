package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.AppInfoReq;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂类
 **/
@Component
public class TaskStrategyFactory implements ApplicationContextAware {

    private Map<String, IBaseTask> map = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IBaseTask> tempMap = applicationContext.getBeansOfType(IBaseTask.class);
        tempMap.values().forEach(iBaseTask -> {
            map.put(iBaseTask.getTaskType(), iBaseTask);
        });
    }

    public BaseRspDTO<Object> executeTask(String key, AppInfoReq req) {
        IBaseTask baseTask = map.get(key);
        if (baseTask != null) {
            System.out.println("工厂策略实现类执行");
            return baseTask.execute(req);
        }
        return null;
    }
}
