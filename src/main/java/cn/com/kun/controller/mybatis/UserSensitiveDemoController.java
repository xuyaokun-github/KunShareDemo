package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.UserSensitiveDO;
import cn.com.kun.mapper.UserSensitiveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/mybatsi-user-sensitive-demo")
@RestController
public class UserSensitiveDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserSensitiveDemoController.class);

    @Autowired
    private UserSensitiveMapper userSensitiveMapper;

    @GetMapping("/test")
    public String test(){

        UserSensitiveDO user = new UserSensitiveDO();
        user.setFirstname("333");
        user.setLastname("444");
        user.setEmail("xuyaokun@qq.com");
        user.setCreateTime(new Date());
        userSensitiveMapper.insert(user);
        return "OK";
    }

    @GetMapping("/testSelect")
    public String testSelect(){

        UserSensitiveDO userSensitiveDO = userSensitiveMapper.getUserByEmail("xuyaokun@qq.com");
        return "OK";
    }

}
