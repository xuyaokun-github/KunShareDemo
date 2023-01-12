package cn.com.kun.springframework.core.threadPoolTaskExecutor;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/spring-threadPoolTaskExecutor-demo")
@RestController
public class SpringThreadPoolTaskExecutorDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringThreadPoolTaskExecutorDemoController.class);

    ThreadPoolTaskExecutor executor = null;

    @GetMapping("/test")
    public ResultVo test(){

        //默认是六十秒过期
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(16);
        executor.setThreadNamePrefix("SpringThreadPoolTaskExecutorDemoController-Thread-");
        executor.setQueueCapacity(2);//默认是LinkedBlockingQueue
        executor.initialize();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/submitTask")
    public ResultVo submitTask(){

        for (int i = 0; i < 17; i++) {
            executor.execute(()->{
                ThreadUtils.sleep(2000);
            });
        }
        return ResultVo.valueOfSuccess(null);
    }


}
