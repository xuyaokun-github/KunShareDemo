package cn.com.kun.batch.batchServiceOne;

import cn.com.kun.common.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xuyaokun On 2020/5/19 22:41
 * @desc: 
 */
public class UserMapItemProcessor implements ItemProcessor<UserMap, User> {


    private static final Logger logger = LoggerFactory.getLogger(UserMapItemProcessor.class);

    @Override
    public User process(UserMap userMap) throws Exception {

        //中间处理器
        /*
        接收的是来自读操作提供的UserMap，然后返回一个User给写操作去做具体的后续处理
        读操作主要负责读，数据来自哪里
        中间处理器重在一些信息的封装
        写操作主要负责保存数据，数据该落地保存在哪里
         */
        logger.debug("进入处理器");
        int i = ThreadLocalRandom.current().nextInt(100);
        User user  = new User();
        user.setFirstname("fisrt" + i);
        user.setLastname("kunghsu" + i);
        user.setEmail(i + "@qq.com");
        user.setPhone("135-" +System.currentTimeMillis());

        //后面的写操作，可以使用一些spring自带提供的，注意这里假如返回的是null，spring不会对它做处理
        /*
        源码org.springframework.batch.core.step.item.SimpleChunkProcessor.transform，这里会对返回的对象进行判断，假如不空才会放入outputs中
         */
//        return null;

        return user;
    }

}
