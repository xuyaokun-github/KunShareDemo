package cn.com.kun.springframework.springcloud.feign.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.feign.client.KunwebdemoFeign;
import org.apache.http.NoHttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/feign-NoHttpResponseException")
@RestController
public class FeignNoHttpResponseExceptionDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignNoHttpResponseExceptionDemoController.class);

    @Autowired
    KunwebdemoFeign kunwebdemoFeign;

    @GetMapping("/test")
    public ResultVo test(){

        //请求第三方系统
        ResultVo resultVo = kunwebdemoFeign.result();

        ResultVo res = ResultVo.valueOfSuccess();
        return resultVo;
    }

    @GetMapping("/test2")
    public ResultVo test2(){

        //请求第三方系统
        ResultVo resultVo = null;
        resultVo = kunwebdemoFeign.result();
        resultVo = kunwebdemoFeign.result();
        resultVo = kunwebdemoFeign.result();
        LOGGER.info("执行完毕");
        ResultVo res = ResultVo.valueOfSuccess();
        return resultVo;
    }

    @GetMapping("/test3")
    public ResultVo test3() throws InterruptedException {

        new Thread(()->{
            //请求第三方系统
            ResultVo resultVo = null;
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(1000);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            ThreadUtils.sleep(1000);
            resultVo = kunwebdemoFeign.result();
            LOGGER.info("服务端返回：{}", resultVo);
            LOGGER.info("执行完毕");
        }).start();


        ResultVo res = ResultVo.valueOfSuccess();
        return res;
    }

    @GetMapping("/testAsync")
    public ResultVo testAsync(){

        //请求第三方系统
        for (int j = 0; j < 300; j++) {
            new Thread(()->{
                try {
                    for (int i = 0; i < 1000; i++) {
                        ResultVo resultVo = kunwebdemoFeign.result();
                        LOGGER.info("res：{}", JacksonUtils.toJSONString(resultVo));
                        try {
                            Thread.sleep(70000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if (e instanceof NoHttpResponseException){
                        System.exit(0);
                    }
                }
            }).start();
        }

        
        return ResultVo.valueOfSuccess();
    }
}
