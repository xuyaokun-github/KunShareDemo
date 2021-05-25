package cn.com.kun.common.advice;

import cn.com.kun.common.annotation.SecretAnnotation;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.secret.SecretHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

import static cn.com.kun.common.utils.AESUtils.DEFAULT_KEY;


@ControllerAdvice
public class SecretResponseBodyAdvice implements ResponseBodyAdvice {

    public final static Logger logger = LoggerFactory.getLogger(SecretResponseBodyAdvice.class);

    /**
     * 加解密组件
     */
    @Autowired
    private SecretHelper secretHelper;

    /**
     * 如何让它只拦截含有@SecretAnnotation注解的方法,用hasMethodAnnotation方法
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.hasMethodAnnotation(SecretAnnotation.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        //body就是即将返回给前端的对象
        if (methodParameter.getMethod().isAnnotationPresent(SecretAnnotation.class)) {
            SecretAnnotation secretAnnotation = methodParameter.getMethod().getAnnotation(SecretAnnotation.class);
            //假如需要加密
            if (secretAnnotation.encode()) {
                /**
                 * 现在需要对这个返回值做加密处理
                 */
                try {
                    String appId = "";
                    List<String> appIdList = serverHttpRequest.getHeaders().get("appId");
                    if (appIdList != null && !appIdList.isEmpty()){
                        appId = appIdList.get(0);
                    }
                    if (StringUtils.isEmpty(appId)){
                        //假如请求头不存在应用ID,则使用默认的系统字符串作为应用ID
                        appId = DEFAULT_KEY;
                    }
                    body = secretHelper.encode(body, appId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("SecretResponseBodyAdvice加密后的字符串：{}", JacksonUtils.toJSONString(body));
        return body;
    }


}