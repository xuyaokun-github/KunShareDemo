package cn.com.kun.springframework.batch.batchService1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class MyStepExecutionListener extends StepExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyStepExecutionListener.class);

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        LOGGER.info("Step监听器里获取ReadCount：{} ", stepExecution.getReadCount());
        return super.afterStep(stepExecution);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution);
    }
}
