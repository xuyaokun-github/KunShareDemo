package cn.com.kun.springframework.batch.batchService2;

import cn.com.kun.common.entity.User;
import cn.com.kun.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by xuyaokun On 2020/5/19 22:41
 * @desc: 
 */
@Component
public class MyItemProcessor implements ItemProcessor<User, User> {

    private static final Logger logger = LoggerFactory.getLogger(MyItemProcessor.class);

    @Override
    public User process(User user) throws Exception {

        logger.info("enter {}", ThreadUtils.getCurrentInvokeClassAndMethod());
        //可以做一些处理，不一定要返回原对象相同类型，可以返回其他任意类型
        return user;
    }

}
