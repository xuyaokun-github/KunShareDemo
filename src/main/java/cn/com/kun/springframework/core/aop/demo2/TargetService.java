package cn.com.kun.springframework.core.aop.demo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TargetService.class);

    public void doService(){
        System.out.println("exec target service");
    }


}
