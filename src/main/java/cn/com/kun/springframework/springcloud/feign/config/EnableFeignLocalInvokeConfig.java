package cn.com.kun.springframework.springcloud.feign.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * author:xuyaokun_kzx
 * date:2021/11/17
 * desc:
*/
//@EnableFeignLocalInvoke
@ConditionalOnProperty(prefix = "feign.localinvoke", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Configuration
@Import(FeignClientsLocalInvokeBeanPostProcessor.class)
public class EnableFeignLocalInvokeConfig {

}
