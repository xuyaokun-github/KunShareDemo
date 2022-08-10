package cn.com.kun.service.switchcheck;

import cn.com.kun.component.switchcheck.ISwitchFetcher;
import cn.com.kun.component.switchcheck.SwitchChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwitchCheckerConfig {

    @Autowired
    private SwitchQueryDemoService switchQueryDemoService;

    @Bean
    public SwitchChecker switchChecker(){
        ISwitchFetcher switchFetcher = new ISwitchFetcher() {

            //这里放具体的实现逻辑（开关用什么方式查由用户自由决定）
            @Override
            public boolean fetch(String switchName, boolean defaultFlag) {
                boolean flag;
                try {
                    flag = switchQueryDemoService.querySwitch(switchName);
                }catch (Exception e){
                    //查不到开关或者查询异常时，默认使用的开关
                    flag = defaultFlag;
                }
                return flag;
            }
        };
        return new SwitchChecker(switchFetcher);
    }


}
