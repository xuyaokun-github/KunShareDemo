package cn.com.kun.common.advice;

import cn.com.kun.common.annotation.DesensitizationAnnotation;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.secret.DesensitizationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@ControllerAdvice
public class DesensitizationResponseBodyAdvice implements ResponseBodyAdvice {

    public final static Logger logger = LoggerFactory.getLogger(DesensitizationResponseBodyAdvice.class);

    /**
     * 加解密组件
     */
    @Autowired
    private DesensitizationHelper desensitizationHelper;

    /**
     * 如何让它只拦截含有@SecretAnnotation注解的方法,用hasMethodAnnotation方法
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.hasMethodAnnotation(DesensitizationAnnotation.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        //body就是即将返回给前端的对象
        if (methodParameter.getMethod().isAnnotationPresent(DesensitizationAnnotation.class)) {
                /**
                 * 现在需要对这个返回值做加密处理
                 */
                try {
                    body = desensitizationHelper.desensitization(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        logger.info("DesensitizationResponseBodyAdvice脱敏后的字符串：{}", JacksonUtils.toJSONString(body));
        return body;
    }


}