package cn.com.kun.springframework.springcloud.zuul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Map;

@RequestMapping("/zuul-demo")
@RestController
public class ZuulDemoController {

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/testZuul")
    public String testZuul() throws FileNotFoundException {
        return "kunghsu testZuul result!";
    }

    /**
     * http://localhost:8089/kunshare-zuul/kunsharedemo/kunsharedemo/zuul-demo/test1
     * @return
     */
    @RequestMapping("/test1")
    public String test1(){

        return "test1";
    }

    //模拟一个远程服务的地址
    private String targetUrl = "http://localhost:8089/kunshare-zuul/zuul-hello/test1";

    @RequestMapping("/test2")
    public Object test2(HttpServletRequest request, @RequestBody Map<String, Object> json){
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        MultiValueMap<String, String> headers = getHeaders(request);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(json, headers);
        ResponseEntity responseEntity = restTemplate.exchange(targetUrl, httpMethod, httpEntity, Object.class);
        //错误的返回
        return responseEntity;
    }

    /**
     * 正确的转发器
     *
     * @param request
     * @param json
     * @return
     */
    @RequestMapping("/test3")
    public Object test3(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> json){
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        MultiValueMap<String, String> headers = getHeaders(request);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(json, headers);
        ResponseEntity responseEntity = restTemplate.exchange(targetUrl, httpMethod, httpEntity, Object.class);
        return responseEntity.getBody();
    }

    private MultiValueMap<String,String> getHeaders(HttpServletRequest request) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.add(key, value);
        }
        return map;
    }

}
