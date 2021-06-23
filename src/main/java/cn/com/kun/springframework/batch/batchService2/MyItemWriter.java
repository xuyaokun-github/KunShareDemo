package cn.com.kun.springframework.batch.batchService2;

import cn.com.kun.bean.entity.User;
import cn.com.kun.mapper.UserMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuyaokun On 2020/5/27 23:15
 * @desc: 
 */
@Component
public class MyItemWriter implements ItemWriter<User> {

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
        System.out.println("cn.com.kun.springframework.batch.batchServiceTwo.MyItemWriter.write");
        if (list == null){
            //其实这个空判断基本多余，spring内部会判断，假如为空，不会进入写操作
            System.out.println("list为空，不做处理");
            return;
        }
        System.out.println(list.size());
        System.out.println(JSONObject.toJSONString(list));

        for(User user : list){
            if ("333".equals(user.getFirstname())){
                throw new RuntimeException("主动模拟抛出的异常");
            }
            userMapper.insert(user);
        }

    }

}
