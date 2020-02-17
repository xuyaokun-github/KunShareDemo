package cn.com.kun.common.configload.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        //这两种，都是放在项目内的，就是jar包内的resources下
        @PropertySource("*:customConfig/login/login.properties"),
        //customConfig可以不写，默认会拼这个作为配置目录，可以通过命令行参数去改写这个
        //所以customConfig/logout/logout.properties和logout/logout.properties都是支持的
        @PropertySource("*:customConfig/logout/logout.properties")
//        ,
        //下面这种，就是放在jar包外的，和jar包同级下的路径
//        @PropertySource("::customConfig/wnbaplay/wnbaplay.properties")

})
public class CustomPropertyConfiguration {

    //所有业务的配置，都可以定义在这个类中
    @Bean
    @ConfigurationProperties(prefix = "login")
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "logout")
    public LogoutProperties logoutProperties() {
        return new LogoutProperties();
    }

    //不定义在这里也可以，在类上用
    /*
    @Component
    @ConfigurationProperties(prefix = "wnbaplay")
    参考WNbaplayProperties类
     */


}
