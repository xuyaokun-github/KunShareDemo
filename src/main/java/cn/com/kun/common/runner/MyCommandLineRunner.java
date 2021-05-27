package cn.com.kun.common.runner;

import cn.com.kun.common.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public final static Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);

    public void run(String... args) {

        logger.info("==================系统启动完成==================");
        /**
         * spring通过自动配置方式注册的数据源名字默认叫dataSource
         *
         */
        Map<String, DataSource> beanMap = context.getBeansOfType(DataSource.class);
        if (beanMap != null){
            System.out.println(JSONObject.toJSONString(beanMap.keySet()));
        }

        //启动一个日志线程循环输出日志
        new Thread(()->{
            while (true){
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("I am log. " + DateUtils.now());
            }
        }, "MyCommandLineRunner-Thread").start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
