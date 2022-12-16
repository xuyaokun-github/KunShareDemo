package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import cn.com.kun.springframework.batch.common.BatchRateLimitDynamicCheckScheduler;
import cn.com.kun.springframework.batch.common.JobRateLimitQueryFunction;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * batch demo1
 * 例子：读取普通文件，处理完后写入数据库或者调接口发送
 *
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
*/
@Configuration
//@EnableBatchProcessing注解必须要加(加在启动类就可以，不用每个config类都加)，否则无法注入JobBuilderFactory
//@EnableBatchProcessing
public class BatchDemo1JobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 定义一个Job（job在容器里是一个单例）
     * @param listener
     * @return
     */
    @Bean
    public Job myFirstJob(MyFirstBatchJobListener listener, MyStepExecutionListener myStepExecutionListener) {
        return jobBuilderFactory.get("myFirstJob")
                .incrementer(new RunIdIncrementer()) //每次运行的ID生成器
                .listener(listener) //指定使用的监听器
                .flow(myFirstStep(myStepExecutionListener)) //指定使用的步骤
                .end()
                .build();
    }

    @Bean
    public Step myFirstStep(MyStepExecutionListener myStepExecutionListener) {
        /*
        定义一个Step,step里会指定用到哪些写操作，读操作
         */
        return stepBuilderFactory.get("importStep")
//                .<UserFileItem, User>chunk(5000)
                .<UserFileItem, User>chunk(3)
                .reader(reader(null)) //这里为了避免编译报错，需要传个null
                .processor(processor(null)) //这里为了避免编译报错，需要传个null
//                .writer(myBatisBatchItemWriter()) //写DB
                .writer(customSendItemWriter(null)) //自定义写操作
                .taskExecutor(myFirstBatchJobExecutor()) //用线程池
                .listener(myStepExecutionListener)
                .build();
    }

    //定义一个读操作
    @Bean
    @StepScope
    public ItemStreamReader<UserFileItem> reader(@Value("#{jobParameters[sourceFilePath]}") String sourceFilePath) {

        //创建FlatFileItemReader
        UserFileItemReader reader = new UserFileItemReader();
        reader.setJobName("myFirstJob");
//        reader.setResource(new FileSystemResource("D:\\home\\kunghsu\\big-file-test\\batchDemoOne-middle-file.txt"));
        //读取文件系统下的文件，通常用绝对路径(测试大文件OOM问题)
        //大文件，每行5M
//        reader.setResource(new FileSystemResource("D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file.txt"));
        //大文件，每行1M
//        reader.setResource(new FileSystemResource("D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file-oneline-1m.txt"));
//        reader.setResource(new FileSystemResource(sourceFilePath));
        //读取classpath下的文件
//        reader.setResource(new ClassPathResource("demoData/batch/batchDemoOne.txt"));
        reader.setResource(new ClassPathResource("demoData/batch/batchDemoOne2.txt"));
        reader.setLineMapper(new DefaultLineMapper<UserFileItem>() {{
            setLineTokenizer(new DelimitedLineTokenizer("|") {{
                setNames(new String[]{"uid", "tag", "type"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<UserFileItem>() {{
                setTargetType(UserFileItem.class);
            }});
        }});
        //使用自定义的LineMapper
        reader.setLineMapper(new LineMapper<UserFileItem>() {
            @Override
            public UserFileItem mapLine(String line, int lineNumber) throws Exception {
                if (StringUtils.isNotEmpty(line)){
                    String[] strArr = line.split("\\|");
                    UserFileItem item = new UserFileItem();
                    item.setUid(Long.parseLong(strArr[0]));
                    item.setTag(strArr[1]);
                    item.setType(Integer.parseInt(strArr[2]));
                    return item;
                }
                return null;
            }
        });
        return reader;
    }

    /**
     * 定义一个介于读写之间的中间处理操作
     * 读和写操作一般都是些通用操作，例如读文件，写库，spring提供很多现成的实现，方便开发
     * 但中间操作这种是个性化的，所以框架不提供
     * @return
     */
    @Bean
    @StepScope
    public UserFileItemItemProcessor processor(@Value("#{jobParameters[jobName]}") String jobName) {
        return new UserFileItemItemProcessor(jobName);
    }


    //定义一个写操作(写DB)
    @Bean
    @StepScope
    public MyBatisBatchItemWriter<User> myBatisBatchItemWriter(){

        //使用Mybatis提供的写操作类
        MyBatisBatchItemWriter<User> writer = new MyBatisBatchItemWriter<>();
        //这个在xml文件里定义的插入语句的id,必须全局唯一
        writer.setStatementId("cn.com.kun.mapper.UserMapper.insert");
        writer.setSqlSessionFactory(sqlSessionFactory);
        return writer;
    }

    @Bean
    @StepScope
    public ItemWriter<User> customSendItemWriter(@Value("#{stepExecution}") StepExecution stepExecution){

        return new CustomSendItemWriter(stepExecution);
    }

    @Bean
    public TaskExecutor myFirstBatchJobExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("myFirstBatchJobExecutor-Thread-");
        //默认是LinkedBlockingQueue
        executor.setQueueCapacity(20);
        return executor;
    }


    //开启批处理动态限流功能
    @Bean
    public BatchRateLimitDynamicCheckScheduler batchRateLimitDynamicCheckScheduler(JobRateLimitQueryFunction jobRateLimitQueryFunction) {

        BatchRateLimitDynamicCheckScheduler scheduler = new BatchRateLimitDynamicCheckScheduler(jobRateLimitQueryFunction);
        return scheduler;
    }


}
