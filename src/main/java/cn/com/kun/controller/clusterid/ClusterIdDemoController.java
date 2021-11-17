package cn.com.kun.controller.clusterid;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.clusterid.snowflake.ClusterIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CountDownLatch;

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

    /**
     * 多线程测试是OK的
     * 多线程并发，没有出现ID重复
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/testMoreThread")
    public ResultVo<String> testMoreThread() throws InterruptedException {

        int threadCount = 8;
        List<String> idList = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {

            new Thread(()->{
                int count = 0;
                List<String> tempList = new ArrayList<>();
                while (true){
                    tempList.add(String.valueOf(clusterIdGenerator.id()));
                    count++;
                    if (count == 30000){
                        break;
                    }
                }
                idList.addAll(tempList);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        Set<String> idSet = new HashSet<>(idList);
        LOGGER.info("比较结果：{},{}", idList.size(), idSet.size());

        if (idList.size() != idSet.size()){
            System.exit(1);
        }

        return ResultVo.valueOfSuccess("");
    }

}
