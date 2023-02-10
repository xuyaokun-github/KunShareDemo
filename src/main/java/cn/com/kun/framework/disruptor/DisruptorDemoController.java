package cn.com.kun.framework.disruptor;

import cn.com.kun.framework.disruptor.demo1.DisruptorMqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/disruptor-demo")
@RestController
public class DisruptorDemoController {

    private final static Logger log = LoggerFactory.getLogger(DisruptorDemoController.class);

    @Autowired
    private DisruptorMqService disruptorMqService;
    /**
     * 项目内部使用Disruptor做消息队列
     * @throws Exception
     */
    @GetMapping("/sayHelloMqTest")
    public void sayHelloMqTest() throws Exception{

        disruptorMqService.sayHelloMq("消息到了，Hello world!" + System.currentTimeMillis());
        log.info("消息队列已发送完毕");
        //这里停止2000ms是为了确定是处理消息是异步的
        Thread.sleep(2000);
    }


}
