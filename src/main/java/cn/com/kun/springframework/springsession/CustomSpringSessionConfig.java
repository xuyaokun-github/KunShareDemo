package cn.com.kun.springframework.springsession;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@ConditionalOnProperty(prefix = "kunsharedemo.redis-session", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
//@EnableRedisHttpSession
//spring在多长时间后强制使redis中的session失效,默认是1800.(单位/秒)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 70 * 30)
public class CustomSpringSessionConfig {


}
