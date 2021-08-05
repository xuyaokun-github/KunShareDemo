package cn.com.kun.controller.spring;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@RequestMapping("/spring-taskExecutor")
@RestController
public class SpringTaskExecutorDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(SpringDemoController.class);

    @Autowired
    Executor myBizCommonExecutor;

    /**
     * CorePoolSize:4
     * MaxPoolSize:100
     * QueueCapacity:10
     * 创建线程的数据不建议太大，假如队列满了，会创建MaxPoolSize个线程
     *
     * @return
     */
    @GetMapping("/testMyBizCommonExecutor")
    public ResultVo testMyBizCommonExecutor(@RequestParam String size){

        for (int i = 0; i < Integer.parseInt(size); i++) {
            try {
                myBizCommonExecutor.execute(()->{
                    LOGGER.info("{},working....", Thread.currentThread().getName());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ResultVo.valueOfSuccess(0);
    }

    @GetMapping("/testShowExecutor")
    public ResultVo testShowExecutor(){

        String str = ((ThreadPoolTaskExecutor)myBizCommonExecutor).getThreadPoolExecutor().toString();
        LOGGER.info("testShowExecutor: {}", str);
        return ResultVo.valueOfSuccess(0);
    }

}
