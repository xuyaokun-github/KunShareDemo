package cn.com.kun.config.spring;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置
 */
@Configuration
public class RestTemplateConfig {

    //简单连接工厂
//    @Bean
//    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
//        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(5000);//ms
//        factory.setConnectTimeout(10000);//ms
//        return factory;
//    }

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate(newClientHttpRequestFactory());
//        List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();
        //添加FormHttpMessageConverter，支持application/x-www-form-urlencoded的Content-Type
//        messageConverterList.add(new FormHttpMessageConverter());
        //默认情况下不需要添加FormHttpMessageConverter，因为已经自带AllEncompassingFormHttpMessageConverter
        //AllEncompassingFormHttpMessageConverter是FormHttpMessageConverter的子类
        return restTemplate;
    }

    /**
     * 自定义RestTemplate配置
     * 1、设置最大连接数
     * 2、设置路由并发数
     * 3、设置重试次数
     * @author Hux
     * @return
     */
    public static ClientHttpRequestFactory newClientHttpRequestFactory() {

        //创建连接池管理器
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager();
        // 长连接保持时长30秒
//        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        // 最大连接数
        pollingConnectionManager.setMaxTotal(3000);
        // 单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(100);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        //开启自动清理过期连接机制
        httpClientBuilder.evictExpiredConnections();
        // 重试次数2次，并开启
//        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接底层配置clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectionRequestTimeout(5000);//ms
        clientHttpRequestFactory.setReadTimeout(5000);//ms
        clientHttpRequestFactory.setConnectTimeout(15000);//ms
        return clientHttpRequestFactory;
    }


}
