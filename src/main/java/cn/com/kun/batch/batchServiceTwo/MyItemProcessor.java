package cn.com.kun.batch.batchServiceTwo;

import cn.com.kun.common.vo.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by xuyaokun On 2020/5/19 22:41
 * @desc: 
 */
@Component
public class MyItemProcessor implements ItemProcessor<User, User> {


    @Override
    public User process(User user) throws Exception {

        //可以做一些处理，不一定要返回原对象相同类型，可以返回其他任意类型
        return user;
    }

}
