package cn.com.kun.springframework.springcloud.feign.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.feign.service.KunShareClientOneFeignService;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/feign")
@RestController
public class FeignDemoController {

    private final static Logger logger = LoggerFactory.getLogger(FeignDemoController.class);

    @Autowired
    private KunShareClientOneFeignService kunShareClientOneFeignService;

    @Autowired
    private KunwebdemoFeign kunwebdemoFeign;

    @GetMapping("/test1")
    public ResultVo test1(){

        ResultVo res = kunShareClientOneFeignService.result();
        logger.info("result:{}", JacksonUtils.toJSONString(res));
        return res;
    }

    @GetMapping("/test2")
    public ResultVo test2(){

        ResultVo res = kunwebdemoFeign.result();
        logger.info("result:{}", JacksonUtils.toJSONString(res));
//        People people = new People();
//        people.setFirstname("tracy");
//        res = otherClientFeignService.result2(people);
//        logger.info("result:{}", JacksonUtils.toJSONString(res));
//        res = otherClientFeignService.result3(people, "myheader-source");
//        logger.info("result:{}", JacksonUtils.toJSONString(res));
        return res;
    }


}
