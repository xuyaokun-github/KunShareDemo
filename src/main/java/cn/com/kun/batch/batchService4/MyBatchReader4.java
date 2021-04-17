package cn.com.kun.batch.batchService4;

import cn.com.kun.common.entity.User;
import cn.com.kun.mapper.UserMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义数据库层作为reader,提供数据
 * MyBatchReader4的作用域将会是Step
 *
 * Created by xuyaokun On 2020/5/24 23:00
 * @desc:
 */
/*
    假如是通过@Bean注解定义，不需要加@Component。
    同时即使没有@Component注解，@PostConstruct也会生效
 */
public class MyBatchReader4 implements ItemReader<User> {


    //用一个索引值记录读取到的位置，一般都是这样用
    //为什么，因为要知道是否读取完，在读取完时及时返回null，终止读操作
    private int nextIndex;

    List<User> userList = new ArrayList<>();

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init(){
        /*
            在这个方法里，就是通过自定义的逻辑去把目标数据查出来
            可以是读取文件，或者是查询数据库
            无论是以哪种方式查出数据，最终把数据放入待处理的集合userList里即可
         */

        //可以使用多个不同的dao层，执行多条sql,把数据查出来，然后聚合
        Map map = new HashMap();
        map.put("firstname", "xu");
        List<User> result = userMapper.query(map);
        userList.addAll(result);
        map.put("firstname", "xie11");
        result = userMapper.query(map);
        userList.addAll(result);
        map.put("firstname", "xie66");
        result = userMapper.query(map);
        userList.addAll(result);
    }

    @Override
    public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        User user = null;
        if (nextIndex < userList.size()) {
            user = userList.get(nextIndex);
            nextIndex++;
        }

        //返回null，表示读取输入已经应该停止了，不用再继续读取（springbatch设计就要这样写）
        //只要返回的内容不为null，都会继续调read，因此一旦写错很容易会造成死循环，就好比索引值写错
        return user;
    }

}
