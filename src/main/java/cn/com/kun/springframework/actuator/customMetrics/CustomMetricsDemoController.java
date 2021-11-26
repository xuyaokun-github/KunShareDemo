package cn.com.kun.springframework.actuator.customMetrics;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.actuator.customMetrics.enums.CustomMetricsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 */
@RequestMapping("/customMetricsDemo")
@RestController
public class CustomMetricsDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomMetricsDemoController.class);

    @Autowired
    private CustomMetricsService customMetricsService;

    @GetMapping("/test")
    public ResultVo test() throws Exception {
        customMetricsService.gather();
        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/testByOptimization")
    public ResultVo testByOptimization() throws Exception {

        //优化后
        customMetricsService.gather(CustomMetricsEnum.MYCUSTOMMETRICS_ONE.counterName,
                ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                "order",
                "xyk");
        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/testByOptimization2OneTimes")
    public ResultVo testByOptimization2OneTimes() throws Exception {

        //优化后
        customMetricsService.gather(CustomMetricsEnum.MYTWO.counterName,
                ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                "order",
                "kunghsu",
                "" + UUID.randomUUID().toString());

        return ResultVo.valueOfSuccess("OK");
    }

    @GetMapping("/testByOptimization2")
    public ResultVo testByOptimization2() throws Exception {

        for (int i = 0; i < 10; i++) {
            ThreadUtils.runAsyncByRunnable(()->{
                while (true){
                    //优化后
                    customMetricsService.gather(CustomMetricsEnum.MYTWO.counterName,
                            ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                            "order",
                            "kunghsu",
                            "" + UUID.randomUUID().toString());
                }
            });
        }


        return ResultVo.valueOfSuccess("OK");
    }


    @GetMapping("/testByOptimization2BySleepOneSeconds")
    public ResultVo testByOptimization2BySleepOneSeconds() throws Exception {

        for (int i = 0; i < 10; i++) {
            ThreadUtils.runAsyncByRunnable(()->{
                while (true){
                    //优化后
                    customMetricsService.gather(CustomMetricsEnum.MYTWO.counterName,
                            ThreadLocalRandom.current().nextInt(10) % 2 == 0 ? "Y" : "N",
                            "order",
                            "kunghsu",
                            "" + UUID.randomUUID().toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        return ResultVo.valueOfSuccess("OK");
    }

}
