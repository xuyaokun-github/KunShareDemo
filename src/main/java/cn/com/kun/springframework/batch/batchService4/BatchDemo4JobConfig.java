package cn.com.kun.springframework.batch.batchService4;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.exception.SkippableException;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.batch.batchService1.UserFileItem;
import cn.com.kun.springframework.batch.common.BatchCommonCountListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * batch demo4
 * 例子：读取数据库记录，中间处理后写入到数据库
 * 这里读库和写库的操作都是用mybatis自带的工具类
 * 优点是方便，但是也有局限性，无法再进一步对查询出来的结果进一步处理再执行read
 * 所以必须要在sql层将要处理的数据筛选好
 *
 * author:xuyaokun_kzx
 * date:2021/5/21
 * desc:
 */
@Configuration
public class BatchDemo4JobConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchDemo4JobConfig.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //定义一个读操作
    @Autowired
    @Qualifier("job4Reader4ForCustom")
    private ItemReader job4Reader4ForCustom;

    @Autowired
    @Qualifier("job4Writer4ForCustom")
    private ItemWriter job4Writer4ForCustom;

    @Value("${kunghsu.batch.skipRatioThreshold:70}")
    private double skipRatioThreshold;


    /**
     * 定义一个Job
     * @param listener（这个监听器是一个单例，全局共用的）
     * @return
     */
    @Bean
    public Job myJob4(BatchCommonCountListener listener) {
        return jobBuilderFactory.get("myJob4")
                .incrementer(new RunIdIncrementer()) //每次运行的ID生成器
                .listener(listener) //指定使用的监听器
                .flow(myJob4Step()) //指定使用的步骤
                .end()
                .build();
    }

    /**
     * chunk属于step下的概念
     * @return
     */
    @Bean
    public Step myJob4Step() {
        /*
        定义一个Step,step里会指定用到哪些写操作，读操作
         */
        return stepBuilderFactory.get("myBatchStep4")
                .<UserFileItem, User>chunk(3)
                //假如出现MyBatchBussinessException超过2次，则job就会终止（前面成功的chunk会被正常提交，不受skip-limit机制影响）

                .reader(job4Reader4ForCustom)
                .processor(myBatchProcessor4(null))
                .writer(job4Writer4ForCustom)
                .faultTolerant()
                .skip(SkippableException.class)
                .skipPolicy(new RatioCheckingSkipPolicy(skipRatioThreshold)) //自定义skip判断策略
//                .skipLimit(4)
                //假如异常被自定义异常处理器跳过了，则skip-limit机制就不会再统计到。
//                .exceptionHandler(new MyBatchExceptionHandler()) //设置异常处理器
//                .taskExecutor(null) //可以指定多线程执行
                .build();
    }

    //定义一个介于读写之间的中间处理操作
    @Bean
    @StepScope
    public MyBatchProcessor4 myBatchProcessor4(@Value("#{stepExecution}") StepExecution stepExecution) {
        return new MyBatchProcessor4(stepExecution);
    }


    /**
     * 配置itemReader
     * 本例子用的是MyBatisCursorItemReader，它直接执行一条sql拿到一批结果，springbatch会遍历这批结果
     * 每次处理多少条，取决于chunk的设置
     * 但是读取肯定不是分页读取的
     *
     * @param firstname
     * @return
     */
    @Bean("job4Reader4ForCustom")
    @StepScope
    public MyBatisCursorItemReader<User> job4Reader4ForCustom(@Value("#{jobParameters[firstname]}") String firstname) {

        /**
         * 这里通过#{jobParameters[firstname]}获取JobParameters的参数
         * JobParameters是在run job之前就组装好的，传给job
         * job在跑批创建Reader时会传入，这里的Reader生命周期是Step,必须是重新创建才能做到每次入参不同，所以是step作用域
         * 注解@StepScope是必须加的，否则编译启动会失败
         */
        MyBatisCursorItemReader<User> reader = new MyBatisCursorItemReader<>();
        //组织查询参数
        Map<String, Object> map = new HashMap<>();
        map.put("firstname", firstname);
        logger.info("组装查询数据库参数为：{}", JacksonUtils.toJSONString(map));
        //指定dao层的语句id
        reader.setQueryId("cn.com.kun.mapper.UserMapper.query");
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setParameterValues(map);

        return reader;
    }

    //定义一个写操作
    @Bean("job4Writer4ForCustom")
    public MyBatisBatchItemWriter<User> job4Writer4ForCustom(){

        //使用Mybatis提供的写操作类
        MyBatisBatchItemWriter<User> writer = new MyBatisBatchItemWriter<>();
        //这个在xml文件里定义的插入语句的id,必须全局唯一（建议加上命名空间，更加具体）
        writer.setStatementId("cn.com.kun.mapper.UserMapper.insert");
        writer.setSqlSessionFactory(sqlSessionFactory);
        return writer;
    }




}
