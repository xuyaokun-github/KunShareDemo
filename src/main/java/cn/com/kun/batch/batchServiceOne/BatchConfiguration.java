package cn.com.kun.batch.batchServiceOne;

import cn.com.kun.common.vo.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
//@EnableBatchProcessing注解必须要加(加在启动类就可以，不用每个config类都加)，否则无法注入JobBuilderFactory
//@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 定义一个Job
     * @param listener
     * @return
     */
    @Bean
    public Job myFirstJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer()) //每次运行的ID生成器
                .listener(listener) //指定使用的监听器
                .flow(myFirstStep()) //指定使用的步骤
                .end()
                .build();
    }

    @Bean
    public Step myFirstStep() {
        /*
        定义一个Step,step里会指定用到哪些写操作，读操作
         */
        return stepBuilderFactory.get("importStep")
                .<UserMap, User>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(myBatisBatchItemWriter())
                .build();
    }

    //定义一个读操作
    @Bean
    public FlatFileItemReader<UserMap> reader() {

        //创建FlatFileItemReader
        FlatFileItemReader<UserMap> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("batch/batchDemoOne.txt"));
        reader.setLineMapper(new DefaultLineMapper<UserMap>() {{
            setLineTokenizer(new DelimitedLineTokenizer("|") {{
                setNames(new String[]{"uid", "tag", "type"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<UserMap>() {{
                setTargetType(UserMap.class);
            }});
        }});
        return reader;
    }

    //定义一个介于读写之间的中间处理操作
    @Bean
    public UserMapItemProcessor processor() {
        return new UserMapItemProcessor();
    }


    //定义一个写操作
    @Bean
    public MyBatisBatchItemWriter<User> myBatisBatchItemWriter(){

        //使用Mybatis提供的写操作类
        MyBatisBatchItemWriter<User> writer = new MyBatisBatchItemWriter<>();
        writer.setStatementId("insert");//这个在xml文件里定义的插入语句的id,必须全局唯一
        writer.setSqlSessionFactory(sqlSessionFactory);

        return writer;
    }



}
