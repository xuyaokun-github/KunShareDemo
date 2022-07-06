package cn.com.kun.controller.parallelInvoke;

import cn.com.kun.foo.javacommon.completableFuture.completionService.ParallelQueryAppHeadPageInfoService;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.AppInfoReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParallelInvokeController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ParallelInvokeController.class);

    @Autowired
    private ParallelQueryAppHeadPageInfoService parallelQueryAppHeadPageInfoService;

    @GetMapping("/test")
    public String testString(){

        AppInfoReq req = new AppInfoReq();
        parallelQueryAppHeadPageInfoService.parallelQueryAppHeadPageInfo2(req);
        return "kunghsu";
    }
}
