package cn.com.kun.springframework.springcloud.feign.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * Feign接口请求拦截器
 * @Description
 *
 **/
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * @description: 将test设置到请求头
     */
    @Override
    public void apply(RequestTemplate template) {
//        String traceId = TraceIdUtil.getTraceId();
//        if (StringUtils.isNotEmpty(traceId)) {
//            template.header("test", "lalala");
//        }
    }


}