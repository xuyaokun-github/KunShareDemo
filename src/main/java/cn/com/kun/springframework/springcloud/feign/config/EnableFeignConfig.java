package cn.com.kun.springframework.springcloud.feign.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 开启Feign
 *
 * author:xuyaokun_kzx
 * date:2021/5/26
 * desc:
*/
@EnableFeignClients(basePackages = {"cn.com.kun.springframework.springcloud.feign"})
@Configuration
public class EnableFeignConfig {

}
