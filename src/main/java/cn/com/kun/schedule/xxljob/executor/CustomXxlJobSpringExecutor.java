package cn.com.kun.schedule.xxljob.executor;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import java.util.Map;

/**
 * 为了修复 xxl-job不支持@Bean+@StepScope定义bean的问题
 * author:xuyaokun_kzx
 * date:2021/5/20
 * desc:
*/
public class CustomXxlJobSpringExecutor extends XxlJobSpringExecutor {

    @Override
    public void afterPropertiesSet() throws Exception {
        // init JobHandler Repository
        if (getApplicationContext() == null) {
            return;
        }

        // init job handler action
        Map<String, Object> serviceBeanMap = getApplicationContext().getBeansWithAnnotation(JobHandler.class);

        if (serviceBeanMap != null && serviceBeanMap.size() > 0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                if (serviceBean instanceof IJobHandler) {
                    String name = serviceBean.getClass().getAnnotation(JobHandler.class).value();
                    IJobHandler handler = (IJobHandler) serviceBean;
                    if (loadJobHandler(name) != null) {
                        throw new RuntimeException("xxl-job jobhandler[" + name + "] naming conflicts.");
                    }
                    registJobHandler(name, handler);
                }
            }
        }
        // init JobHandler Repository (for method)
        //取消initJobHandlerMethodRepository，即可解决问题
//        initJobHandlerMethodRepository(applicationContext);

        // refresh GlueFactory
        GlueFactory.refreshInstance(1);

        // super start
        super.start();
    }
}
