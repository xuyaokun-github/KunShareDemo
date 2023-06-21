package cn.com.kun.springframework.actuator.customMetrics.countdemo;

import cn.com.kun.common.vo.ResultVo;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 业务监控统计同一个接口中的不同上游渠道的TPS
 *
 * author:xuyaokun_kzx
 * date:2023/6/21
 * desc:
*/
@RequestMapping("/http-count-demo")
@RestController
public class HttpVisitCountDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpVisitCountDemoController.class);

    @Autowired
    private MeterRegistry meterRegistry;

    private String[] srcChannelIdArray = new String[]{"aaa", "bbb", "ccc"};

    private String[] templateIdArray = new String[]{"template1", "template2", "template3"};

    /**
     * 模拟多个上游请求接口，从而进行计数
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/mockHttpVisit")
    public ResultVo mockHttpVisit() throws Exception {

        new Thread(()->{
            for (int i = 0; i < 10; i++) {

                new Thread(()->{
                    while (true){
                        try {
                            Thread.sleep(ThreadLocalRandom.current().nextLong(500));
                            recordMetrics(srcChannelIdArray[ThreadLocalRandom.current().nextInt(3)],
                                    templateIdArray[ThreadLocalRandom.current().nextInt(3)]);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            //开启一个100TPS的线程
            new Thread(()->{
                while (true){
                    try {
                        Thread.sleep(10);
                        recordMetrics("ddd",
                                templateIdArray[ThreadLocalRandom.current().nextInt(3)]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }).start();



        return ResultVo.valueOfSuccess("OK");
    }

    /**
     * 统计业务监控
     *
     * @param srcChannelId
     * @param template
     */
    private void recordMetrics(String srcChannelId, String template) {

        //可以使用多个tag 根据业务需要
        Counter.builder("My_Http_Counter")
                .tag("srcChannelId", srcChannelId)
                .tag("template", template)
                .register(meterRegistry).increment();
    }


}
