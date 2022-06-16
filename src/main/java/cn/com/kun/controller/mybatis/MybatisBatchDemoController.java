package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.mybatis.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/mybatis-batch-demo")
@RestController
public class MybatisBatchDemoController {

    private final static Logger logger = LoggerFactory.getLogger(MybatisBatchDemoController.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private UserService userService;

    @GetMapping("/testBatch")
    public ResultVo testBatch(){

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setFirstname("Firstname" + i);
            user.setLastname("Lastname" + i);
            userList.add(user);
        }
        boolean flag = userService.saveBatch(userList);
        return ResultVo.valueOfSuccess(flag);
    }

}
