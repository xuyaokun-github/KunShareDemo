package cn.com.kun.zipkin.controller;


import cn.com.kun.common.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/zipkin")
@RestController
public class ZipkinDemoController {

    public final static Logger logger = LoggerFactory.getLogger(ZipkinDemoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/test")
    public String test(HttpServletRequest request){

        String result;// = restTemplate.getForObject("http://localhost:8081/zipkin/test", String.class);
        result = HttpClientUtils.doGet("http://localhost:8081/zipkin/test", null);
        logger.info(result);
        return "success";
    }

}
