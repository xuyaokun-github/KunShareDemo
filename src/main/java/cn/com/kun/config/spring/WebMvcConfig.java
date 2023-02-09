package cn.com.kun.config.spring;

import cn.com.kun.common.format.MyDateFormat;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.DateFormat;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    /**
     * @Description 静态资源路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        boolean enabledFastjson = Boolean.parseBoolean(env.getProperty("kunsharedemo.fastjson.enabled"));
        if (enabledFastjson){
            // 1、需要先定义一个·convert转换消息的对象；
            FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
            // 2、添加fastjson的配置信息，比如 是否要格式化返回json数据
            FastJsonConfig fastJsonConfig = new FastJsonConfig();
            fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
            // 3、在convert中添加配置信息.
            fastConverter.setFastJsonConfig(fastJsonConfig);
            // 4、将convert添加到converters当中.
            converters.add(0, fastConverter);//使用fastjson作为默认
//        converters.add(fastConverter);//使用jackson作为默认
        }

    }

    /**
     * 自定义一个DateFormat
     * @return
     */
//    @Bean
    public MappingJackson2HttpMessageConverter MappingJsonpHttpMessageConverter() {

        ObjectMapper mapper = jackson2ObjectMapperBuilder.build();
        // ObjectMapper为了保障线程安全性，里面的配置类都是一个不可变的对象
        // 所以这里的setDateFormat的内部原理其实是创建了一个新的配置类
        DateFormat dateFormat = mapper.getDateFormat();
        mapper.setDateFormat(new MyDateFormat(dateFormat));
        MappingJackson2HttpMessageConverter mappingJsonpHttpMessageConverter = new MappingJackson2HttpMessageConverter(
                mapper);
        return mappingJsonpHttpMessageConverter;
    }

    /**
     * 只有5.3以上版本支持PathPatternParser
     * @param configurer
     */
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.setPatternParser(new PathPatternParser());
//    }
}
