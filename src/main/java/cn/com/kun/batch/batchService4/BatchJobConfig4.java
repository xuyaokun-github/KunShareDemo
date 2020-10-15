package cn.com.kun.batch.batchService4;

import cn.com.kun.batch.batchServiceOne.JobCompletionNotificationListener;
import cn.com.kun.batch.batchServiceOne.UserMap;
import cn.com.kun.common.vo.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BatchJobConfig4 {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //定义一个读操作
    @Autowired
    @Qualifier("reader4")
    ItemReader reader4;

    /**
     * 定义一个Job
     * @param listener（这个监听器是一个单例，全局共用的）
     * @return
     */
    @Bean
    public Job myBatchJob4(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("myBatchJob4")
                .incrementer(new RunIdIncrementer()) //每次运行的ID生成器
                .listener(listener) //指定使用的监听器
                .flow(myBatchStep4()) //指定使用的步骤
                .end()
                .build();
    }

    @Bean
    public Step myBatchStep4() {
        /*
        定义一个Step,step里会指定用到哪些写操作，读操作
         */
        return stepBuilderFactory.get("myBatchStep4")
                .<UserMap, User>chunk(2)
                .reader(reader4)
                .processor(myBatchProcessor4())
                .writer(myBatchWriter4())
                .exceptionHandler(new MyBatchExceptionHandler()) //设置异常处理器
                .build();
    }

    //配置itemReader
    @Bean("reader4")
    @StepScope
    MyBatisCursorItemReader<User> reader4(@Value("#{jobParameters[firstname]}") String firstname) {

        System.out.println("开始查询数据库");
        MyBatisCursorItemReader<User> reader = new MyBatisCursorItemReader<>();
        //组织查询参数
        Map<String, Object> map = new HashMap<>();
        map.put("firstname", firstname);
        //指定dao层的语句id
        reader.setQueryId("cn.com.kun.mapper.UserMapper.query");
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(map);

        return reader;
    }

    //定义一个介于读写之间的中间处理操作
    @Bean
    public MyBatchProcessor4 myBatchProcessor4() {
        return new MyBatchProcessor4();
    }


    //定义一个写操作
    @Bean
    public MyBatisBatchItemWriter<User> myBatchWriter4(){

        //使用Mybatis提供的写操作类
        MyBatisBatchItemWriter<User> writer = new MyBatisBatchItemWriter<>();
        writer.setStatementId("insert");//这个在xml文件里定义的插入语句的id,必须全局唯一
        writer.setSqlSessionFactory(sqlSessionFactory);

        return writer;
    }



}
