package cn.com.kun.springframework.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 批处理通用计数监听器
 * Created by xuyaokun On 2020/10/20 22:13
 * @desc: 
 */
@Component
public class BatchCommonCountListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(BatchCommonCountListener.class);

    /**
     * 在任务开始时会执行
     * @param jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        //初始化一个计数器对象
        BatchExecCounter.initCountId();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        //通过jobParameters获取执行前传入的一些参数，例如任务名，需要保存到数据库中的名字
        JobParameters jobParameters = jobExecution.getJobParameters();

        //这里还可以获取到读取到的总个数、提交的总个数等
        //一个job中可能有很多step(通常我们只关注第一个，大多数情况下用一个step就完事了)
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        int readCount = 0;
        for (StepExecution stepExecution : stepExecutions){
            readCount = readCount + stepExecution.getReadCount();
        }
        logger.info("read的总个数：" + readCount);

        BatchExecCounter.CountData countData = BatchExecCounter.getCountData();
        BatchExecCounter.removeCountId();
        long sucesssNum = countData.getSuccessNum().get();
        long failNum = countData.getFailNum().get();
        //插入数据库（通过数据库保存批处理每个任务的执行成功失败次数）
        //TODO
        logger.info(String.format("存库,成功%s条，失败%s条", sucesssNum, failNum));

    }
}
