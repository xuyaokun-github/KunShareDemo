package cn.com.kun.common.configload.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigLoadDemoController {

    @Autowired
    LoginProperties loginProperties;

    @Autowired
    LogoutProperties logoutProperties;

    @Autowired
    NbaplayProperties nbaplayProperties;

//    @Autowired
    WNbaplayProperties wNbaplayProperties;

    @RequestMapping("/testConfigLoadDemoController")
    public String test1(){

        //原来的全局配置属性
        System.out.println(JSONObject.toJSONString(nbaplayProperties));
        System.out.println(JSONObject.toJSONString(loginProperties));
        System.out.println(JSONObject.toJSONString(logoutProperties));
        System.out.println(JSONObject.toJSONString(wNbaplayProperties));

        return "success!!";
    }

}
