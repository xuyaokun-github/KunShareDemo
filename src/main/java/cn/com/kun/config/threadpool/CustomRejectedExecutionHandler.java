package cn.com.kun.config.threadpool;

import cn.com.kun.controller.spring.SpringDemoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池抛弃策略
 * 逻辑：先重试三次重新进入队列，假如都失败了，再用当前线程运行
 *
 * author:xuyaokun_kzx
 * date:2021/8/5
 * desc:
*/
@Component
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

    public final static Logger LOGGER = LoggerFactory.getLogger(SpringDemoController.class);

    int maxCount = 3;

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        boolean isAdded = false;
        int count = maxCount;
        //可以设置重试次数
        for (int i = 0; i < count; i++) {
            //尝试重新进队列
            LOGGER.info("第{}次尝试重新入队", i + 1);
            try {
                isAdded = executor.getQueue().offer(r, 2, TimeUnit.SECONDS);
                if (isAdded){
                    LOGGER.info("重新入队成功");
                    break;
                }
            } catch (InterruptedException e) {
                LOGGER.info("重新入队异常");
            }
        }

        if (!isAdded && !executor.isShutdown()) {
            //假如重试三次都没成功，就用当前线程运行
            LOGGER.info("重新入队失败,用当前线程运行：{}", Thread.currentThread().getName());
            r.run();
        }
    }


}
