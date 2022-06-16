package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomSendItemWriter implements ItemWriter<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFileItemItemProcessor.class);

    @Override
    public void write(List<? extends User> items) throws Exception {

        for(User user : items){
            LOGGER.info("写操作阶段处理：{}", JacksonUtils.toJSONString(user));
        }
        //这里拿到的一个chunk的总条数
        LOGGER.info("本次处理总条数：{}", items.size());
    }

}
