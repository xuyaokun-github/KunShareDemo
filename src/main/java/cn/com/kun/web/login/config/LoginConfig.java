package cn.com.kun.web.login.config;

import cn.com.kun.web.login.filter.SessionLoginFilter;
import cn.com.kun.web.login.interceptor.JWTLoginInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 登录的配置:过滤器
 * Created by xuyaokun On 2020/10/26 22:27
 * @desc:
 */
@Configuration
public class LoginConfig {

    @Configuration
    @ConditionalOnProperty(
            value = {"kunsharedemo.login.type"},
            havingValue = "session"
    )
    protected static class SessionLoginFilterConfiguration {

        @Bean
        public SessionLoginFilter sessionLoginFilter() {
            return new SessionLoginFilter();
        }
    }

    @Configuration
    @ConditionalOnProperty(
            value = {"kunsharedemo.login.type"},
            havingValue = "jwt"
    )
    protected static class WebInterceptConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            // LogInterceptor apply to all URLs.
            registry.addInterceptor(new JWTLoginInterceptor());
        }

    }

}
