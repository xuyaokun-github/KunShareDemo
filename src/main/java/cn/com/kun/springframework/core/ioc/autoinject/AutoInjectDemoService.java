package cn.com.kun.springframework.core.ioc.autoinject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoInjectDemoService {

    @Autowired
    AutoInjectDemoService2 autoInjectDemoService2;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
