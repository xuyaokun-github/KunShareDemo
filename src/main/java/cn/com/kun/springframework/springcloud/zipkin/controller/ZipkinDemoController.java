package cn.com.kun.springframework.springcloud.zipkin.controller;


import cn.com.kun.common.utils.HttpClientUtils;
import cn.com.kun.common.utils.ThreadMdcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executor;

@RequestMapping("/zipkin-demo")
@RestController
public class ZipkinDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(ZipkinDemoController.class);

    @Qualifier("myFirstTaskExecutor")
    @Autowired
    Executor myFirstTaskExecutor;

    @Qualifier("mySecondTaskExecutor")
    @Autowired
    Executor mySecondTaskExecutor;


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/test")
    public String test(HttpServletRequest request){

        String result;// = restTemplate.getForObject("http://localhost:8081/zipkin/test", String.class);
        result = HttpClientUtils.doGet("http://localhost:8081/zipkin/test", null);
        LOGGER.info(result);
        return "success";
    }

    @RequestMapping(value = "/invokeThird")
    public String invokeThird(HttpServletRequest request){

        String result = restTemplate.getForObject("http://localhost:8091/kunwebdemo/hello", String.class);
//        result = HttpClientUtils.doGet("http://localhost:8081/zipkin/test", null);
        LOGGER.info(result);
        return "success";
    }


    /**
     * 例子，子线程丢失了traceId
     * @param request
     * @return
     */
    @RequestMapping(value = "/testNoTrace")
    public String testNoTrace(HttpServletRequest request){

        LOGGER.info("testNoTrace");
        new Thread(()->{
            LOGGER.info("sub thread:testNoTrace");
        }).start();

        return "success";
    }

    /**
     * 例子，子线程依然持有traceId
     * @param request
     * @return
     */
    @RequestMapping(value = "/testHasTrace")
    public String testHasTrace(HttpServletRequest request){

        LOGGER.info("testHasTrace");
        new Thread(ThreadMdcUtil.wrap(()->{
            LOGGER.info("sub thread:testHasTrace");
        })).start();

        return "success";
    }

    /**
     * 例子，线程池里的任务是否丢失了traceId
     * @param request
     * @return
     */
    @RequestMapping(value = "/testNoTraceBySpringTaskExecutor")
    public String testNoTraceBySpringTaskExecutor(HttpServletRequest request){

        LOGGER.info("testNoTraceBySpringTaskExecutor");
        myFirstTaskExecutor.execute(()->{
            LOGGER.info("sub thread testNoTraceBySpringTaskExecutor");
        });

        return "success";
    }

    /**
     * 例子，线程池里的任务丢失了traceId
     * @param request
     * @return
     */
    @RequestMapping(value = "/testHasTraceBySpringTaskExecutor")
    public String testHasTraceBySpringTaskExecutor(HttpServletRequest request){

        LOGGER.info("testHasTraceBySpringTaskExecutor");
        mySecondTaskExecutor.execute(()->{
            LOGGER.info("sub thread testHasTraceBySpringTaskExecutor");
        });

        return "success";
    }

    @RequestMapping(value = "/testMuitlTask")
    public String testMuitlTask(HttpServletRequest request){

        LOGGER.info("testMuitlTask");

        for (int i = 0; i < 5; i++) {
//            new Thread(()->{
//
//            }).start();
            myFirstTaskExecutor.execute(()->{
                LOGGER.info("sub thread testMuitlTask");
            });
        }

        return "success";
    }

    /**
     * LazyTraceThreadPoolTaskExecutor的TraceRunnable会在任务执行完时清空traceid
     * 所以并不会出现traceId重复的问题！
     * @param request
     * @return
     */
    @RequestMapping(value = "/testMuitlTask2")
    public String testMuitlTask2(HttpServletRequest request){

        LOGGER.info("testMuitlTask2");

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                myFirstTaskExecutor.execute(()->{
                    LOGGER.info("sub thread testMuitlTask2");
                });
            }).start();
        }
        return "success";
    }


}
