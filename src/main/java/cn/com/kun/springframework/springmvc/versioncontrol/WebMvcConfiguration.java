package cn.com.kun.springframework.springmvc.versioncontrol;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * spring mvc版本控制
 *
 * author:xuyaokun_kzx
 * date:2023/8/3
 * desc:
*/
@Configuration
public class WebMvcConfiguration implements WebMvcRegistrations {

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new PathVersionHandlerMapping();
    }
}