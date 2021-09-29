package cn.com.kun.springframework.springcloud.zuul.service;

import cn.com.kun.component.ratelimiter.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ZuulDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZuulDemoService.class);

    /**
     * 首先将调用第三方平台的接口的逻辑封装成一个方法入口
     * @param paramMap
     */
    @RateLimit(bizSceneName = "sendmsg", key = "#sendChannel")
    public void invoke(Map<String, String> paramMap, String sendChannel){
        /*
         * 第一个需求：
         * 这里会调各种第三方平台的接口
         * 现在需要控制调每个平台的频率，其实就是需要实现向后限流
         * 后表示的就是第三方平台，因为每个平台的能力不同，所以需要以不同的限流配置进行限流
         * 如何根据
         */
        LOGGER.info("ZuulDemoService在调用第三方接口");

    }


}
