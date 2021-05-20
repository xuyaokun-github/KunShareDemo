package cn.com.kun.springframework.batch.batchService4;

import cn.com.kun.common.entity.User;
import cn.com.kun.mapper.UserMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by xuyaokun On 2020/5/27 23:15
 * @desc: 
 */
public class MyBatchWriter4 implements ItemWriter<User> {

    @Autowired
    private UserMapper userMapper;

    /**
     * 每次都是传入一个待处理的集合
     * 每次传入多少个由chunk的长度决定，可以在定义step时指定
     * @param list
     * @throws Exception
     */
    @Override
    public void write(List<? extends User> list) throws Exception {

        for(User user : list){
            userMapper.insert(user);
        }

    }

}
