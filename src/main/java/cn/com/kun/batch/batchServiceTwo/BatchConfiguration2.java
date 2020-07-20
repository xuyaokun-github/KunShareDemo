package cn.com.kun.batch.batchServiceTwo;

import cn.com.kun.batch.batchServiceOne.UserMap;
import cn.com.kun.common.vo.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration2 {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    //自定义读操作
    @Autowired
    ItemReader myItemReader;
    //自定义中间操作
    @Autowired
    ItemProcessor myItemProcessor;
    //自定义写操作
    @Autowired
    ItemWriter myItemWriter;

    /**
     * 定义一个Job
     * @return
     */
    @Bean
    public Job mySecondJob() {
        return jobBuilderFactory.get("mySecondJob")
                .incrementer(new RunIdIncrementer()) //每次运行的ID生成器
                .flow(mySecondStep()) //指定使用的步骤
                .end()
                .build();
    }

    @Bean
    public Step mySecondStep() {
        /*
        定义一个Step,step里会指定用到哪些写操作，读操作
         */
        return stepBuilderFactory.get("mySecondStep")
                .<UserMap, User>chunk(2)
                .reader(myItemReader)
                .processor(myItemProcessor)
                .writer(myItemWriter)
                .build();
    }
    

}
