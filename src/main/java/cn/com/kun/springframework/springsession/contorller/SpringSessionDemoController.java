package cn.com.kun.springframework.springsession.contorller;


import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/spring-session")
@RestController
public class SpringSessionDemoController {


    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public String set(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        request.getSession().setAttribute("request Url", request.getRequestURL());
        map.put("request Url", request.getRequestURL());
        map.put("当前请求的sessionId", request.getSession().getId());
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/set2", method = RequestMethod.GET)
    public String set2(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        return JSON.toJSONString(map);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("message", request.getSession().getAttribute("request Url"));
        return JSON.toJSONString(map);
    }
}
