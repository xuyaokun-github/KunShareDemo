package cn.com.kun.springframework.core.beanDefinition;

import cn.com.kun.common.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class BeanDefinitionDemoService {

    private static Logger LOGGER = LoggerFactory.getLogger(BeanDefinitionDemoService.class);

//    @Autowired
    BeanDefinitionHelloService beanDefinitionHelloService;

    public void method() {

        Object beanDefinitionHelloService = SpringContextUtil.getBean(BeanDefinitionHelloService.class);
        LOGGER.info("{}", beanDefinitionHelloService);
        BeanDefinitionHelloService service = (BeanDefinitionHelloService) ((LinkedHashMap)beanDefinitionHelloService).get("beanDefinitionHelloService");
        service.sayHello();
    }

}
