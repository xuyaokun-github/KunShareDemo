package cn.com.kun.springframework.springcloud.eureka.controller;


import cn.com.kun.service.redisson.RedissonDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RequestMapping("/eureka")
@RestController
public class EurekaDemoController implements ApplicationContextAware {

    public final static Logger logger = LoggerFactory.getLogger(EurekaDemoController.class);

    private ApplicationContext context;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate loadBalancedRestTemplate;

    @PostConstruct
    public void init(){
        Map<String, RestTemplate> beanMap = context.getBeansOfType(RestTemplate.class);
        if (beanMap != null){
            logger.info("容器里RestTemplate的bean个数：" + beanMap.size());
            beanMap.forEach((k,v)->{
                logger.info("===============RestTemplate beanMap:" + k);
            });
        }

    }

    @RequestMapping("/testHello")
    public String testHello(){

        String result = restTemplate.getForObject("http://eureka-client-one/one/test", String.class);
        System.out.println(result);
        return "kunghsu testHello";
    }

    @RequestMapping("/testHello2")
    public String testHello2(){

        String result = loadBalancedRestTemplate.getForObject("http://eureka-client-one/one/test", String.class);
        System.out.println(result);
        return "kunghsu testHello";
    }

    /**
     * 用负载均衡
     * @return
     */
    @RequestMapping("/testBatch")
    public String testBatch(){

        invokeBatch("http://eureka-client-two/two/testHello", 1);

        return "kunghsu testHello2";
    }

    /**
     * 不用负载均衡
     * @return
     */
    @RequestMapping("/testBatch2")
    public String testBatch2(){

        invokeBatch("http://127.0.0.1:8082/two/testHello", 1);
        return "kunghsu testHello2";
    }


    /**
     *
     * @param url
     * @param count （启动多少个线程调用这个url）
     */
    private void invokeBatch(String url, int count){
        new Thread(()->{
            long start = System.currentTimeMillis();
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for (int i = 0; i < count; i++) {
                new Thread(()->{
                    String result = restTemplate.getForObject(url, String.class);
                    System.out.println(result);
                    countDownLatch.countDown();
                }).start();
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("耗时：" + (System.currentTimeMillis() - start));
        }).start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }



}
