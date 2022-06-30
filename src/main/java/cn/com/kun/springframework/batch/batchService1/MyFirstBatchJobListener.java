package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.springframework.batch.common.BatchProgressRateCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class MyFirstBatchJobListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(MyFirstBatchJobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        //不需要，可以不重写
        super.beforeJob(jobExecution);
        //放入一个统一计数器，记录处理的行数
        BatchProgressRateCounter.initCounter();
    }


    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.debug("enter {}", ThreadUtils.getCurrentInvokeClassAndMethod());
        logger.info("进入监听器end .....");
        //一般会在这里做一些记录，用一个自定义表保存该次批处理执行的结果信息
        //移除计数器
        BatchProgressRateCounter.removeCounter();
    }

}
