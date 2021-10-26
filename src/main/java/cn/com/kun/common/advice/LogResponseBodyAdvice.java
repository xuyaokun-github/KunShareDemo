package cn.com.kun.common.advice;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * 拦截返回的ResponseBody做日志记录
 * 慎用，这个是有性能消耗的，虽然很小但也不能完全忽略，可以通过开关控制，在测试环境才开启
 */
@ControllerAdvice
public class LogResponseBodyAdvice implements ResponseBodyAdvice {

    private final static Logger logger = LoggerFactory.getLogger(LogResponseBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Method method=methodParameter.getMethod();
        String url=serverHttpRequest.getURI().toASCIIString();
        logger.info("{}.{},url:{},result:{}", method.getDeclaringClass().getSimpleName(),
                method.getName(), url, JSON.toJSONString(o));
        return o;
    }
}