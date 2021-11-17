package cn.com.kun.springframework.springcloud.feign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class MyLocalFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {


        //Caused by: java.lang.IllegalArgumentException: url values must be not be absolute.
//        template.uri("http://127.0.0.1:8091");

        //拿到的是这种格式：/kunwebdemo/feigndemo/test
        String sourceUrl = template.url();

        /*
            在拦截器里能复写url,但是不能复写ip和端口
         */
        template.uri("/kunwebdemo/feigndemo/test");

    }


}
