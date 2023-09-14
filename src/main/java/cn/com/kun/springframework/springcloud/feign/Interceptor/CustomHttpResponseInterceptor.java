package cn.com.kun.springframework.springcloud.feign.Interceptor;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CustomHttpResponseInterceptor implements HttpResponseInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomHttpResponseInterceptor.class);

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        LOGGER.info("进入CustomHttpResponseInterceptor");
        context.getAttribute("");
        response.getEntity();
    }

}
