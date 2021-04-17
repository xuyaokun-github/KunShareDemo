package cn.com.kun.web.login.controller;

import cn.com.kun.common.entity.User;
import cn.com.kun.common.utils.AESUtils;
import cn.com.kun.web.login.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;

import static cn.com.kun.web.login.util.JwtUtil.AES_KEY;

/**
 *
 * Created by xuyaokun On 2020/10/26 23:51
 * @desc:
 */
@RequestMapping("/logindemo4Jwt")
@RestController
public class JwtLoginController {

    @PostMapping("/loginByJwt")
    public String login(@RequestBody User user) throws Exception {
        // 判断账号密码是否正确，这一步肯定是要读取数据库中的数据来进行校验的，这里为了模拟就省去了
        if ("admin".equals(user.getUsername()) && "admin".equals(user.getPassword())) {
            // 如果登录成功，就返回生成的token
            /**
             * 这里登录成功之后，只基于用户名来生成token
             * 这里还可以放入多个内容
             */
            //generate的入参是一个字符串，可以传入sso实体的json串
            //为了模拟，先设置一分钟过期
            Date expiryDate = new Date(System.currentTimeMillis() + Duration.ofMinutes(1).toMillis());
            String token = JwtUtil.generate(user.getUsername(), expiryDate);
            token = AESUtils.encrypt(token, AES_KEY);
            return token;
        }
        return "账号密码错误";
    }

    @GetMapping(value = "/api")
    public String api() {
        return "api成功返回数据";
    }

    @GetMapping(value = "api2")
    public String api2() {
        return "api2成功返回数据";
    }


}
