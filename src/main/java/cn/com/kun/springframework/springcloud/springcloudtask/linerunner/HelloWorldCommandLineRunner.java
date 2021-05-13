package cn.com.kun.springframework.springcloud.springcloudtask.linerunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class HelloWorldCommandLineRunner implements CommandLineRunner {

    public final static Logger logger = LoggerFactory.getLogger(HelloWorldCommandLineRunner.class);

    @Override
    public void run(String... strings) throws Exception {

//        if (true){
//            //模拟一个异常
//            int a = 1/0;
//        }
        logger.info("running a CommandLineRunner task!!!!");
    }
}