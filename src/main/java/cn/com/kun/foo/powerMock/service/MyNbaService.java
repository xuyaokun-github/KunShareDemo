package cn.com.kun.foo.powerMock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MyNbaService {

    @Autowired
    Environment environment;

    @Autowired
    private RocketService rocketService;

    public String play(){
        System.out.println("playing");
        return "playing!!";
    }

    public String playByRocket(){
        System.out.println(environment.getProperty("123"));
        System.out.println("invoke playByRocket");
        return rocketService.sayRocket();
    }

}
