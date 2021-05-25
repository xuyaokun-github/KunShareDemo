package cn.com.kun.common.advice;

import cn.com.kun.common.annotation.SecretAnnotation;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.secret.SecretHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import static cn.com.kun.common.utils.AESUtils.DEFAULT_KEY;

@RestControllerAdvice
public class SecretRequestBodyAdvice implements RequestBodyAdvice {

    public final static Logger logger = LoggerFactory.getLogger(SecretRequestBodyAdvice.class);

    /**
     * 加解密组件
     */
    @Autowired
    private SecretHelper secretHelper;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        /**
         * 只拦截加了注解RequestBody的请求参数
         * 通常一个控制层方法，只接收一个请求参数，约定是用post方法，因为get方法拿不到具体的RequestBody内容
         */
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        if (methodParameter.getMethod().isAnnotationPresent(SecretAnnotation.class)) {
            SecretAnnotation secretAnnotation = methodParameter.getMethod().getAnnotation(SecretAnnotation.class);
            //假如需要解密
            if (secretAnnotation.decode()) {
                return new HttpInputMessage() {
                    @Override
                    public InputStream getBody() throws IOException {
                        String appId = "";
                        List<String> appIdList = httpInputMessage.getHeaders().get("appId");
                        if (appIdList != null && !appIdList.isEmpty()){
//                            throw new RuntimeException("请求头缺少appID");
                            appId = appIdList.get(0);
                        }
                        if (StringUtils.isEmpty(appId)){
                            //假如请求头不存在应用ID,则使用默认的系统字符串作为应用ID
                            appId = DEFAULT_KEY;
                        }
                        String bodyStr = IOUtils.toString(httpInputMessage.getBody(),"utf-8");
                        logger.info("SecretRequestBodyAdvice解密前的字符串：{}", bodyStr);
                        try {
                            //注意，type.getClass()拿到的是java.lang.Class
                            //type本身就是一个class对象
                            logger.info("SecretRequestBodyAdvice待解密的class类型：{}", type.getClass());
                            //利用json工具类，将请求入参转成java对象，其实就是控制层方法要接收的参数
                            Object targetObj = JacksonUtils.toJavaObject(bodyStr, (Class)type);
                            //然后针对对象利用反射做解密， 再将对象转成字符串
                            bodyStr = secretHelper.decode(targetObj, appId);
                            logger.info("SecretRequestBodyAdvice解密后的字符串：{}", bodyStr);
                        } catch (Exception e) {
                            //解密失败
                            logger.error("SecretRequestBodyAdvice解密失败", e);
                        }
                        return  IOUtils.toInputStream(bodyStr,"utf-8");
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return httpInputMessage.getHeaders();
                    }
                };
            }
        }
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

}
