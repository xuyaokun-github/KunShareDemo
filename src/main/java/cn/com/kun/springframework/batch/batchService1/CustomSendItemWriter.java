package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import cn.com.kun.springframework.batch.common.BatchProgressRateCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomSendItemWriter implements ItemWriter<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFileItemItemProcessor.class);

    @Override
    public void write(List<? extends User> items) throws Exception {

//        for(User user : items){
//            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(user));
//        }

        //并行流
        items.stream().parallel().forEach(item->{
//            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(item));
        });

        //这里拿到的一个chunk的总条数
        LOGGER.info("本次处理总条数：{}", items.size());
        //如何输出总条数呢？
        BatchProgressRateCounter.add(items.size());

        LOGGER.info("总共处理条数：{}", BatchProgressRateCounter.getProgressCount());

    }

}
