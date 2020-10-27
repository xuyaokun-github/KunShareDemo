package cn.com.kun.web.login.controller;

import cn.com.kun.common.vo.User;
import cn.com.kun.web.login.context.RequestContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/logindemo")
@RestController
public class SessionLoginController {

    @PostMapping("login")
    public String login(@RequestBody User user, HttpSession session) {
        if ("admin".equals(user.getUsername()) && "admin".equals(user.getPassword())) {
            session.setAttribute("user", user);
            return "登录成功";
        }
        return "账号或密码错误";
    }

    @GetMapping("api")
    public String api() {
        // 各种业务操作
        return "api成功返回数据";
    }

    @GetMapping("api2")
    public String api2() {
        // 各种业务操作
        User user = RequestContext.getCurrentUser();
        return "api2成功返回数据";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "退出成功";
    }
}
