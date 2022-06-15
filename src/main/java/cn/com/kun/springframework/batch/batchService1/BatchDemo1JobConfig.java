package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
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
import org.springframework.core.io.FileSystemResource;

/**
 * batch demo1
 * 例子：读取普通文件，处理完后写入数据库
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
    public Job myFirstJob(MyFirstBatchJobListener listener) {
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
                .<UserFileItem, User>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(myBatisBatchItemWriter())
                .build();
    }

    //定义一个读操作
    @Bean
    public FlatFileItemReader<UserFileItem> reader() {

        //创建FlatFileItemReader
        FlatFileItemReader<UserFileItem> reader = new FlatFileItemReader<>();
        //读取文件系统下的文件，通常用绝对路径
        reader.setResource(new FileSystemResource("D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file.txt"));
        //读取classpath下的文件
//        reader.setResource(new ClassPathResource("demoData/batch/batchDemoOne.txt"));
        reader.setLineMapper(new DefaultLineMapper<UserFileItem>() {{
            setLineTokenizer(new DelimitedLineTokenizer("|") {{
                setNames(new String[]{"uid", "tag", "type"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<UserFileItem>() {{
                setTargetType(UserFileItem.class);
            }});
        }});
        return reader;
    }

    /**
     * 定义一个介于读写之间的中间处理操作
     * 读和写操作一般都是些通用操作，例如读文件，写库，spring提供很多现成的实现，方便开发
     * 但中间操作这种是个性化的，所以框架不提供
     * @return
     */
    @Bean
    public UserFileItemItemProcessor processor() {
        return new UserFileItemItemProcessor();
    }


    //定义一个写操作
    @Bean
    public MyBatisBatchItemWriter<User> myBatisBatchItemWriter(){

        //使用Mybatis提供的写操作类
        MyBatisBatchItemWriter<User> writer = new MyBatisBatchItemWriter<>();
        //这个在xml文件里定义的插入语句的id,必须全局唯一
        writer.setStatementId("cn.com.kun.mapper.UserMapper.insert");
        writer.setSqlSessionFactory(sqlSessionFactory);
        return writer;
    }



}
