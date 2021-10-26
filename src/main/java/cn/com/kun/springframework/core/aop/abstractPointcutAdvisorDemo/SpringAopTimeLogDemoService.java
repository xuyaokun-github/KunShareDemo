package cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import org.springframework.stereotype.Service;

@Service
public class SpringAopTimeLogDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringAopTimeLogDemoService.class);

    private Sleeper sleeper = new ThreadWaitSleeper();


    @TimeLog
    public String method() throws InterruptedException {

        LOGGER.info("cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo.SpringAopTimeLogDemoService.method");
        sleeper.sleep(1000);
        return "target method";
    }


}
