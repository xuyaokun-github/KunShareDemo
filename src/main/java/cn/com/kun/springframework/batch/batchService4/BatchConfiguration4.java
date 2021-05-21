package cn.com.kun.springframework.batch.batchService4;

import cn.com.kun.common.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
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
 * Created by xuyaokun On 2020/5/26 22:36
 * @desc: 
 */
@Configuration
public class BatchConfiguration4 {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //配置一个Job
    @Bean
    Job myJob3() {
        return jobBuilderFactory.get("myJob3")
                .start(myStep3())
                .build();
    }

    //定义一个读操作
    @Autowired
    @Qualifier("myItemReader3")
    ItemReader myItemReader3;

    @Autowired
    @Qualifier("myItemWriter3")
    ItemWriter myItemWriter3;

    //配置一个Step
    @Bean
    Step myStep3() {
        return stepBuilderFactory.get("myStep3")
                .<User, User>chunk(2)
                .reader(myItemReader3)
                .writer(myItemWriter3)
                .build();
    }


    //配置itemReader
    @Bean("myItemReader3")
    @StepScope
    MyBatisCursorItemReader<User> myItemReader3(@Value("#{jobParameters[firstname]}") String firstname) {

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



    //配置itemWriter
    @Bean("myItemWriter3")
    @StepScope
    FlatFileItemWriter<User> myItemWriter3(@Value("#{jobParameters[date]}") String date) {

        System.out.println("开始写入文件中");
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
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
