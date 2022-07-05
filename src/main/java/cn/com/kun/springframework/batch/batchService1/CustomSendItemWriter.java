package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import cn.com.kun.springframework.batch.common.BatchProgressRateCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomSendItemWriter implements ItemWriter<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFileItemItemProcessor.class);

    private StepExecution stepExecution;

    public CustomSendItemWriter(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends User> items) throws Exception {

        String jobInstanceId = String.valueOf(stepExecution.getJobExecution().getJobInstance().getInstanceId());
//        for(User user : items){
//            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(user));
//        }

        //并行流
        items.stream().parallel().forEach(item->{
//            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(item));
        });

        //这里拿到的一个chunk的总条数
        //如何输出总条数呢？
        BatchProgressRateCounter.add(jobInstanceId, items.size());
        LOGGER.info("本次处理总数：{} " +
                        "当前任务实例处理总数：{}  " +
                        "当前任务实例Read总数：（来自批处理框架）：{} " +
                        "当前任务实例Write总数：（来自批处理框架）：{} " +
                        "当前任务实例Skip总数：（来自批处理框架）：{}",
                items.size(),
                BatchProgressRateCounter.getProgressCount(jobInstanceId),
                stepExecution.getReadCount(),
                stepExecution.getWriteCount(),
                stepExecution.getSkipCount());
    }

}
