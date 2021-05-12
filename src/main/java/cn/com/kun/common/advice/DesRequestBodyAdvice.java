package cn.com.kun.common.advice;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * RequestBodyAdvice的用法
 * Created by xuyaokun On 2021/4/25 22:55
 * @desc: 
 */
@ControllerAdvice //注解必不可少
public class DesRequestBodyAdvice implements RequestBodyAdvice {

    public final static Logger logger = LoggerFactory.getLogger(DesRequestBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;//返回false表示关闭该RequestBodyAdvice
    }

    /**
     * 拦截请求，可以
     * @param httpInputMessage
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        //可以在这里拦截http请求，记录一下headers,但是改不了http请求的内容
        return httpInputMessage;
    }

    /**
     * 请求体不为空时进入该方法
     * @param o 具体的入参值
     * @param httpInputMessage http请求
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = methodParameter.getMethod();
        logger.info("{}.{}:{}",method.getDeclaringClass().getSimpleName(),method.getName(), JSON.toJSONString(o));
        //这个就是参数值，在这里可以做一些额外的操作，例如加解密，这里return之后，具体的controller层就会收到return后的值
        return o;
    }

    /**
     * 假如请求是空的，就会进这个方法（反之不会进入）
     * Get请求，也可以使用@RequestBody注解接收参数
     * 假如没传参数会报400异常,可以使用@RequestBody(required = false)免抛异常
     *
     * @param o 具体的入参值
     * @param httpInputMessage 相当于一个http请求对象，能获取到headers等信息
     * @param methodParameter 方法参数（@RequestBody只能修饰一个参数，所以这里每次只处理一个参数，表示的只是一个参数）
     * @param type 参数的类型
     * @param aClass 消息处理器的种类
     * @return
     */
    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method=methodParameter.getMethod();
        logger.info("{}.{}",method.getDeclaringClass().getSimpleName(),method.getName());
        return o;
    }
}
