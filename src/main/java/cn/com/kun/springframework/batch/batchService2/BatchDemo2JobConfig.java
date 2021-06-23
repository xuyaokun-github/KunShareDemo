package cn.com.kun.springframework.batch.batchService2;

import cn.com.kun.bean.entity.User;
import cn.com.kun.springframework.batch.batchService1.UserFileItem;
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

/**
 * batch demo2
 * 例子：自定义读操作，从一个已知的集合中读取，处理完后写入数据库
 *
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
 */
@Configuration
public class BatchDemo2JobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //自定义读操作
    @Autowired
    private ItemReader myItemReader;
    //自定义中间操作
    @Autowired
    private ItemProcessor myItemProcessor;
    //自定义写操作
    @Autowired
    private ItemWriter myItemWriter;

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
                .<UserFileItem, User>chunk(2) //定义块长度是2，每到两个元素就开始执行中间操作
                .reader(myItemReader)
                .processor(myItemProcessor)
                .writer(myItemWriter)
                .build();
    }
    

}
