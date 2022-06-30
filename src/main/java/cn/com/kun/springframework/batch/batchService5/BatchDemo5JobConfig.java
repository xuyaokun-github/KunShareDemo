package cn.com.kun.springframework.batch.batchService5;

import cn.com.kun.bean.entity.User;
import cn.com.kun.springframework.batch.common.BatchCommonCountListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.HashMap;
import java.util.Map;

/**
 *  batch demo5
 *  例子：读取数据库记录，中间处理后写入到文件中
 *
 *
 * Created by xuyaokun On 2020/5/26 22:36
 * @desc: 
 */
@Configuration
public class BatchDemo5JobConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchCommonCountListener.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //配置一个Job
    @Bean
    public Job myJob5() {
        return jobBuilderFactory.get("myJob5")
                .start(myJob5Step())
                .build();
    }

    //定义一个读操作
    @Autowired
    @Qualifier("myItemReader5")
    ItemReader myItemReader5;

    @Autowired
    @Qualifier("myItemWriter5")
    ItemWriter myItemWriter5;

    //配置一个Step
    @Bean
    Step myJob5Step() {
        return stepBuilderFactory.get("myJob5Step")
                .<User, User>chunk(2)
                .reader(myItemReader5)
                .writer(myItemWriter5)
                .build();
    }


    //配置itemReader
    @Bean("myItemReader5")
    @StepScope
    MyBatisCursorItemReader<User> myItemReader5(@Value("#{jobParameters[firstname]}") String firstname) {

        logger.info("开始查询数据库");
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



    //配置itemWriter
    @Bean("myItemWriter5")
    @StepScope
    FlatFileItemWriter<User> myItemWriter5(@Value("#{jobParameters[date]}") String date) {

        logger.info("开始写入文件");
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        //实际业务场景，该写入到哪里，一般也是在run job之前就定好了
        writer.setResource(new FileSystemResource("D:\\home\\User.txt"));//系统目录
        //将User对象转换成字符串,并输出到文件
        writer.setLineAggregator(new LineAggregator<User>() {

            @Override
            public String aggregate(User user) {
                //转json,也可以用builder拼接成其他格式
                ObjectMapper mapper = new ObjectMapper();
                String str = null;
                try {
                    //转成json
                    str = mapper.writeValueAsString(user) + "" + date;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return str;
            }

        });

        return writer;
    }

}
