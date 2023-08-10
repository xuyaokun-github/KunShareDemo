package cn.com.kun.springframework.batch.batchService2;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.entity.User;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by xuyaokun On 2020/5/27 23:15
 * @desc: 
 */
@Component
public class MyItemWriter implements ItemWriter<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyItemProcessor.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 每次都是传入一个待处理的集合
     * 每次传入多少个由chunk的长度决定，可以在定义step时指定
     * @param list
     * @throws Exception
     */
    @Override
    public void write(List<? extends User> list) throws Exception {
        LOGGER.info("cn.com.kun.springframework.batch.batchServiceTwo.MyItemWriter.write");
        if (list == null){
            //其实这个空判断基本多余，spring内部会判断，假如为空，不会进入写操作
            LOGGER.info("list为空，不做处理");
            return;
        }
//        LOGGER.info(JSONObject.toJSONString(list));

//        for(User user : list){
//
//            //            if ("333".equals(user.getFirstname())){
////                throw new RuntimeException("主动模拟抛出的异常");
////            }
//            userMapper.insert(user);
//        }

        //尝试复现一个死锁异常
        String idCard = "XKHDYZ";
        Student student1 = new Student();
        student1.setIdCard(idCard);
        student1.setAddress(UUID.randomUUID().toString());
        student1.setStudentName("kunghsu");
        student1.setCreateTime(new Date());
        List<Student> studentList = new ArrayList<>();
        for (int k = 0; k < 1000; k++) {
            studentList.add(student1);
        }
        for (int j = 0; j < 40; j++) {

            studentList.stream().parallel().forEach(obj->{
                int res = studentMapper.insert(obj);
            });
            Thread.sleep(100);
        }

        LOGGER.info("cn.com.kun.springframework.batch.batchServiceTwo.MyItemWriter.write 结束");

    }

}
