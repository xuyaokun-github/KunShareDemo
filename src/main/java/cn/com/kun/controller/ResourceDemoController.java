package cn.com.kun.controller;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static org.springframework.core.io.support.SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION;

@RequestMapping("/resource-demo")
@RestController
public class ResourceDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceDemoController.class);

    /**
     * 模拟springboot加载FACTORIES文件的过程
     * @return
     * @throws IOException
     */
    @GetMapping("/testShowAllFactoriesFileUrl")
    public ResultVo testShowAllFactoriesFileUrl() throws IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = null;
        if (classLoader != null){
            //这个方法进不去
            urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
        }else {
            urls = ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION);
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            LOGGER.info("URL:{}", url.toString());
        }

        return ResultVo.valueOfSuccess(null);
    }


}
