package cn.com.kun.controller.spring;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.config.SpringCloudConfigDemoService;
import cn.com.kun.springframework.springcloud.config.SpringCloudConfigDemoService2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequestMapping("/springcloudconfig")
@RestController
public class SpringCloudConfigDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringCloudConfigDemoController.class);

    @Autowired
    SpringCloudConfigDemoService springCloudConfigDemoService;

    @Autowired
    SpringCloudConfigDemoService2 springCloudConfigDemoService2;

    @Autowired
    RefreshEndpoint refreshEndpoint;

    @GetMapping("/test")
    public ResultVo test(){
        return ResultVo.valueOfSuccess(springCloudConfigDemoService.method());
    }

    @GetMapping("/test2")
    public ResultVo test2(){
        return ResultVo.valueOfSuccess(springCloudConfigDemoService2.methodCustomThreadPoolProperties());
    }

    /**
     * 模拟/refresh接口
     * @return
     */
    @GetMapping("/testRefresh")
    public ResultVo testRefresh(){

        Collection<String> refreshedKeyList = refreshEndpoint.refresh();
        return ResultVo.valueOfSuccess(refreshedKeyList);
    }

}
