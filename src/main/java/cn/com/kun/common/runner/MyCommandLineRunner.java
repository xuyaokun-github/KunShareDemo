package cn.com.kun.common.runner;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
public class MyCommandLineRunner implements CommandLineRunner, ApplicationContextAware {

    ApplicationContext context;

    public void run(String... args) {

        /**
         * spring通过自动配置方式注册的数据源名字默认叫dataSource
         *
         */
        Map<String, DataSource> beanMap = context.getBeansOfType(DataSource.class);
        if (beanMap != null){
            System.out.println(JSONObject.toJSONString(beanMap.keySet()));
        }

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
