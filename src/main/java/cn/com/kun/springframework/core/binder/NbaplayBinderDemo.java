package cn.com.kun.springframework.core.binder;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.service.redisson.RedissonDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class NbaplayBinderDemo {

    public final static Logger LOGGER = LoggerFactory.getLogger(RedissonDemoService.class);

    @Autowired
    Environment environment;

    @Autowired
    NbaplayBinder nbaplayBinder;

    @PostConstruct
    public void init(){

    }

    /**
     * 配置文件：
     * nbaplay.number=1
     * nbaplay.level=super star2
     */
    public void method(){
        //可以将配置文件里的属性绑定到某个对象上
        //例子：这里将nbaplay开头的配置绑定到nbaplayBinder这个对象上
        Binder.get(environment).bind("nbaplay", Bindable.ofInstance(nbaplayBinder));
        LOGGER.info("验证Binder：{}", nbaplayBinder.getNumber());

        //还可以将多个配置解析成map对象
        Map map = Binder.get(environment).bind("nbaplay", Map.class).get();
        //{"number":"1","level":"super star2"}
        LOGGER.info("验证Binder，map：{}", JacksonUtils.toJSONString(map));

    }


}
