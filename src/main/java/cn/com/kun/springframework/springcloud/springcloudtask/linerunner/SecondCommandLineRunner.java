package cn.com.kun.springframework.springcloud.springcloudtask.linerunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class SecondCommandLineRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(SecondCommandLineRunner.class);

    @Override
    public void run(String... strings) throws Exception {
        logger.info("running a SecondCommandLineRunner task!!!!");
    }
}