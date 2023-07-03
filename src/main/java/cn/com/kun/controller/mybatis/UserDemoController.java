package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import cn.com.kun.service.mybatis.multiThreadTranscation.MultiThreadTranscationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/user-demo")
@RestController
public class UserDemoController {

    private final static Logger logger = LoggerFactory.getLogger(UserDemoController.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    private MultiThreadTranscationService multiThreadTranscationService;

    @GetMapping("/test")
    public String test(){

        List<User> userList = userMapper.selectAllByMoreResultMap(0);
        return "OK";
    }

    @Transactional
    @GetMapping("/testSelectAllByMoreResultMap")
    public String testSelectAllByMoreResultMap(){

        List<User> userList = userMapper.selectAllByMoreResultMap(0);
        return "OK";
    }

    @GetMapping("/testUpdate")
    public String testUpdate(){

        User user = new User();
        int res = userService.update(user);
        return "OK";
    }

    @GetMapping("/testUpdate2")
    public String testUpdate2(){

        userService.updateOrderCount2(888L, 1);
        return "OK";
    }

    @GetMapping("/testUpdateMore")
    public String testUpdateMore(){

        User user = new User();
        int res = userService.updateMore(user);
        return "OK";
    }

    /**
     * 验证多线程事务
     * @return
     */
    @GetMapping("/testMultiThreadTranscation")
    public String testMultiThreadTranscation() throws SQLException {

        Long maxId = userMapper.findMaxId();

        String prefix = "MultiThreadTranscation-";
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setId(Long.valueOf(++maxId));
            user.setCreateTime(new Date());
            user.setAge(ThreadLocalRandom.current().nextInt(100));
            user.setLastname(prefix + UUID.randomUUID().toString());
            user.setFirstname(prefix + UUID.randomUUID().toString());
            user.setUsername(prefix + UUID.randomUUID().toString());
            userList.add(user);
        }
        //反例
//        multiThreadTranscationService.saveThread(userList);
        //正例
        multiThreadTranscationService.saveThread2(userList);

        return "OK";
    }


    @GetMapping("/testInsertBatchDeadLock")
    public String testInsertBatchDeadLock(){

        Long maxId = userMapper.findMaxId();
        String prefix = "testInsertBatchDeadLock-";
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setId(Long.valueOf(++maxId));
            user.setCreateTime(new Date());
            user.setAge(ThreadLocalRandom.current().nextInt(100));
            user.setLastname(prefix + UUID.randomUUID().toString());
            user.setFirstname(prefix + UUID.randomUUID().toString());
            user.setUsername(prefix + UUID.randomUUID().toString());
            userList.add(user);
        }

        MultiThreadTranscationService.averageAssign(userList, 5).forEach(list->{
            new Thread(()->{
                try {
                    userMapper.insertByBatch(list);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });

        return "OK";
    }

    /**
     * 单线程复现不出问题(经典的 查询更新查询问题，只有在并发的时候才会出现)
     *
     * @return
     */
    @GetMapping("/testEndlessLoopProblem")
    public String testEndlessLoopProblem(){

        userService.updateOrderCount(888L, 1);
        return "OK";
    }

    /**
     *
     * @return
     */
    @GetMapping("/testEndlessLoopProblemByMoreThread")
    public String testEndlessLoopProblemByMoreThread(){

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {

            new Thread(()->{

                try {
                    countDownLatch.countDown();
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                userService.updateOrderCount(888L, 1);
            }).start();
        }

        return "OK";
    }
}
