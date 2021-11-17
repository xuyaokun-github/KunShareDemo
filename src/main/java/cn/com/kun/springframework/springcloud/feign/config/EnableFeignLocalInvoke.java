package cn.com.kun.springframework.springcloud.feign.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * feign扩展--支持本地调试
 *
 * author:xuyaokun_kzx
 * date:2021/11/17
 * desc:
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignClientsLocalInvokeBeanPostProcessor.class)
public @interface EnableFeignLocalInvoke {

}
