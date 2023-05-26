package cn.com.kun.component.logDesensitization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DesensitizationConfig {

    @Value("${logDesensitization.enabled:true}")
    private boolean desensitizationEnable;


    @PostConstruct
    public void init(){
        if (!desensitizationEnable){
            LogDesensitizationUtils.close();
        }
    }

}
