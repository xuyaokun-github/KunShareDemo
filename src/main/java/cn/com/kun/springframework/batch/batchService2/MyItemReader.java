package cn.com.kun.springframework.batch.batchService2;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MyItemReader.class);

    //用一个索引值记录读取到的位置，一般都是这样用
    //为什么，因为要知道是否读取完，在读取完时及时返回null，终止读操作
    private int nextIndex;

    //从外部读入的一个数据集合
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

        logger.info("enter {}", ThreadUtils.getCurrentInvokeClassAndMethod());
        //待处理集合
        List<User> targetUserList = userList;
        /*
            其实这里也可以设计成查库，举个例子，假如查完库有记录，就继续处理
            每次查出来的数据，处理完之后，要重置这个userList
            并且要有一个标志区分，需不需要做初始化查询
            每当job第一次进入read方法时，就需要做查询，必须要用个标志区分是不是初次进入
            这个标志必须从外部设置！
         */
        //read()会被执行多次，每次执行时，nextIndex都会变！
        User user = null;
        if (nextIndex < targetUserList.size()) {
            user = targetUserList.get(nextIndex);
            nextIndex++;
        }else {
            nextIndex = 0;
        }
        //返回null，表示读取输入已经应该停止了，不用再继续读取（springbatch设计就要这样写）
        //只要返回的内容不为null，都会继续调read，因此一旦写错很容易会造成死循环，就好比索引值写错

        /**
         * 注意事项：
         * nextIndex这个是单例里的属性，所以下一次再执行job，nextIndex值还是旧的
         * 所以每次执行完都要清空，如何清空？
         * 重新置0即可。
         * 但这种要控制好，不能让这个job并发
         *
         * 另一种错误场景，假如nextIndex还没加到最大值，中途出异常了，nextIndex变成了一个中间值
         * 这种情况也要考虑
         * 所以自己控制nextIndex，相比spring来控制会麻烦一点
         */

        return user;
    }

}
