package cn.com.kun.kafka.dataStatMonitor.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LagCheckScheduleTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(LagCheckScheduleTask.class);

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public static void initSchedule() {


        scheduledExecutorService.scheduleAtFixedRate(()->{

            LOGGER.info("LagCheckScheduleTask Running");
            try {
                //检测是否存在堆积中的主题

                //发送“通知”


            }catch (Exception e){
                LOGGER.error("LagCheckScheduleTask异常", e);
            }
        }, 0L, 10L, TimeUnit.SECONDS);
    }
}
