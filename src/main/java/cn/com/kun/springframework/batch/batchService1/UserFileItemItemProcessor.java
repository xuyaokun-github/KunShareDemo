package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.bean.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

/**
 * Created by xuyaokun On 2020/5/19 22:41
 * @desc: 
 */
public class UserFileItemItemProcessor implements ItemProcessor<UserFileItem, User> {


    private static final Logger logger = LoggerFactory.getLogger(UserFileItemItemProcessor.class);

    @Override
    public User process(UserFileItem userFileItem) throws Exception {

        //中间处理器
        /*
        接收的是来自读操作提供的UserFileItem，然后返回一个User给写操作去做具体的后续处理
        读操作主要负责读，数据来自哪里
        中间处理器重在一些信息的封装
        写操作主要负责保存数据，数据该落地保存在哪里
         */
        logger.debug("进入处理器");
        int i = userFileItem.getType();
        User user  = new User();
        user.setFirstname("fisrt" + i);
        user.setLastname("kunghsu" + i);
        user.setEmail(i + "@qq.com");
        user.setPhone("135-" +System.currentTimeMillis());
        user.setCreateTime(new Date());
        //后面的写操作，可以使用一些spring自带提供的，注意这里假如返回的是null，spring不会对它做处理
        /*
        源码org.springframework.batch.core.step.item.SimpleChunkProcessor.transform，这里会对返回的对象进行判断，假如不空才会放入outputs中
         */
//        return null;

        return user;
    }

}
