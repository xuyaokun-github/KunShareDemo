package cn.com.kun.apache.skywalking.service;

import akka.dispatch.forkjoin.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class SkywalkingDemoService22 {

    public void method4() throws InterruptedException {

        Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
    }
}
