package cn.com.kun.service.monitor;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.component.monitor.annotation.MonitorClass;
import cn.com.kun.component.monitor.worker.MonitorWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@MonitorClass(timePeriod = 1, timeUnit = TimeUnit.SECONDS)
@Service
public class MyThirdMonitorWorker implements MonitorWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyThirdMonitorWorker.class);

    @Override
    public boolean doMonitor() {

        LOGGER.info("监控中、date:{} start", DateUtils.now());
        ThreadUtils.sleep(1000);
        LOGGER.info("监控中、date:{} end", DateUtils.now());
        //返回false表示永久执行
        return false;
    }


}
