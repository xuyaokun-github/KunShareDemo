package cn.com.kun.batch.batchServiceTwo;

import cn.com.kun.common.vo.User;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by xuyaokun On 2020/5/24 23:00
 * @desc:
 */
@Component
public class MyItemReader implements ItemReader<User> {


    //用一个索引值记录读取到的位置，一般都是这样用
    //为什么，因为要知道是否读取完，在读取完时及时返回null，终止读操作
    private int nextIndex;

    List<User> userList = new ArrayList<>();

    @PostConstruct
    public void init(){
        //模拟已经有一个集合待读取
        //实际业务中这里可能是读取一个文件，解析所有行放入List中
        User user1 = new User();
        user1.setFirstname("111");
        user1.setLastname("222");
        User user2 = new User();
        user2.setFirstname("333");
        user2.setLastname("444");
        User user3 = new User();
        user3.setFirstname("555");
        user3.setLastname("666");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

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
