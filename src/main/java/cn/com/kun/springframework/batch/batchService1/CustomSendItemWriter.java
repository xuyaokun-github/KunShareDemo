package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.batch.common.BatchProgressRateCounter;
import cn.com.kun.springframework.batch.common.BatchRateLimiterHolder;
import com.google.common.util.concurrent.RateLimiter;
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
        //通过一个ID找到限流器
        String jobName = String.valueOf(stepExecution.getJobExecution().getJobInstance().getJobName());
        String jobId = stepExecution.getJobExecution().getJobParameters().getString("jobId");

        for(User user : items){

            //处理的时候，先判断是否限速
            rateLimit(jobId);

            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(user));
            //模拟一个耗时，验证 续跑场景
            //验证限速时，临时关闭
//            ThreadUtils.sleep(3000);

        }

        //并行流
//        items.stream().parallel().forEach(item->{
//            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(item));
//        });

        //这里拿到的一个chunk的总条数
        //如何输出总条数呢？
        BatchProgressRateCounter.add(jobInstanceId, items.size());
        //临时关闭日志
//        LOGGER.info("本次处理总数：{} " +
//                        "当前任务实例处理总数：{}  " +
//                        "当前任务实例Read总数：（来自批处理框架）：{} " +
//                        "当前任务实例Write总数：（来自批处理框架）：{} " +
//                        "当前任务实例Skip总数：（来自批处理框架）：{}",
//                items.size(),
//                BatchProgressRateCounter.getProgressCount(jobInstanceId),
//                stepExecution.getReadCount(),
//                stepExecution.getWriteCount(),
//                stepExecution.getSkipCount());

    }

    /**
     * 抽成服务层 TODO
     * @param jobName
     */
    private void rateLimit(String jobName) {

        //假如能找到限速器，则进行限速
        RateLimiter rateLimiter = BatchRateLimiterHolder.getRateLimiter(jobName);
        while (true){
            if (!rateLimiter.tryAcquire()){
                //触发限流
                continue;
            }
            break;
        }

    }


}
