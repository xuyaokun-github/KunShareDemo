package cn.com.kun.springframework.springcloud.eureka;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

//@EnableDiscoveryClient
//@Configuration
public class EnableEurekaConfig {


    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(newClientHttpRequestFactory());
        return restTemplate;
    }

    /**
     * 自定义RestTemplate配置
     * 1、设置最大连接数
     * 2、设置路由并发数
     * 3、设置重试次数
     * @return
     */
    public static ClientHttpRequestFactory newClientHttpRequestFactory() {

        // 长连接保持时长30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(
                60, TimeUnit.SECONDS);
        // 最大连接数
        pollingConnectionManager.setMaxTotal(3000);
        // 单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(1000);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数2次，并开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2,
                true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接底层配置clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setReadTimeout(5000);// ms
        clientHttpRequestFactory.setConnectTimeout(15000);//
        return clientHttpRequestFactory;
    }

}
