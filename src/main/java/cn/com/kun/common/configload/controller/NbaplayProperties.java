package cn.com.kun.common.configload.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 这个类的作用主要是为了验证，在引入自定义的属性加载器之后，原来的application.properties不会被影响
 */
@Component
@ConfigurationProperties(prefix = "nbaplay")
public class NbaplayProperties {

    String number;
    String level;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
