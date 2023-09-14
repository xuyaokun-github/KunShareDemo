package cn.com.kun.springframework.springcloud.feign.Interceptor;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;

//@Configuration
public class FeignHttpClientInterceptorConfig {

    /**
     * 自定义HttpClientBuilder，添加拦截器
     *
     * @return
     */
    @Bean
    public HttpClientBuilder customApacheHttpClientBuilder() {

        HttpResponseInterceptor interceptor = new CustomHttpResponseInterceptor();
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder = builder.addInterceptorFirst(interceptor);
        return builder;
    }

}
