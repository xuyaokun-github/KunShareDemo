package cn.com.kun.log.mdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MDCDemoController {

    public final static Logger logger = LoggerFactory.getLogger(MDCDemoController.class);

    @RequestMapping("/testMDC2")
    public String testMDC2(){

        // 入口传入请求ID
        MDC.put("requestId", UUID.randomUUID().toString());

        // 主线程打印日志
        logger.debug("log in main thread");

        // 异步线程打印日志
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.debug("log in other thread");
            }
        }).start();

        // 出口移除请求ID
        //注释掉remove这行，验证线程重用的情况
//        MDC.remove("requestId");

        return "success!!";
    }


    @RequestMapping("/testMDC3")
    public String testMDC3(){

        // 主线程打印日志
        logger.debug("log  by  testMDC3 -----  log in main thread");

        return "success!!";
    }


}
