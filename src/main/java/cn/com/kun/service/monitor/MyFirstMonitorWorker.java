package cn.com.kun.service.monitor;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.component.monitor.worker.MonitorWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MyFirstMonitorWorker implements MonitorWorker {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyFirstMonitorWorker.class);

    @Override
    public boolean doMonitor() {

        LOGGER.info("监控中、date:{} start", DateUtils.now());
        ThreadUtils.sleep(10000);
        LOGGER.info("监控中、date:{} end", DateUtils.now());
        //返回false表示永久执行
        return false;
    }

}
