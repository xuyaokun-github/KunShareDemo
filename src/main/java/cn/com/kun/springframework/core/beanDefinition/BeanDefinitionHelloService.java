package cn.com.kun.springframework.core.beanDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//不需要任何定义，由我们来注册进spring容器
public class BeanDefinitionHelloService {

    private static Logger LOGGER = LoggerFactory.getLogger(BeanDefinitionHelloService.class);

    private String name;

    private String address;

    public void sayHello(){
        LOGGER.info("hello..........");
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }


}
