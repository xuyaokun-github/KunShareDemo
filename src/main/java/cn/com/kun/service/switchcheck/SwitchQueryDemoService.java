package cn.com.kun.service.switchcheck;

import org.springframework.stereotype.Service;

@Service
public class SwitchQueryDemoService {

    boolean querySwitch(String switchName){

        //模拟一个DAO层

        if ("NBA".equals(switchName)){
            return true;
        }
        if ("WNBA".equals(switchName)){
            return false;
        }
        if ("CBA".equals(switchName)){
            return true;
        }

        return false;
    }


}

