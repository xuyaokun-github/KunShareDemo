package cn.com.kun.controller.clusterid;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.clusterid.snowflake.ClusterIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/clusterid-demo")
@RestController
public class ClusterIdDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterIdDemoController.class);

    @Autowired
    ClusterIdGenerator clusterIdGenerator;

    @GetMapping("/test")
    public ResultVo<String> test(){

        for (int i = 0; i < 50; i++) {
            LOGGER.info("" + clusterIdGenerator.id());
        }
        return ResultVo.valueOfSuccess("");
    }



}
