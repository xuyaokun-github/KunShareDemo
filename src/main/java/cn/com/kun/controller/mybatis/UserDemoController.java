package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user-demo")
@RestController
public class UserDemoController {

    private final static Logger logger = LoggerFactory.getLogger(UserDemoController.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserService userService;

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

    @GetMapping("/testUpdateMore")
    public String testUpdateMore(){

        User user = new User();
        int res = userService.updateMore(user);
        return "OK";
    }
}
