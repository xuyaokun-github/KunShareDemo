package cn.com.kun.service.monitor;

import cn.com.kun.component.monitor.MonitorManager;
import cn.com.kun.component.monitor.worker.MonitorWorker;
import cn.com.kun.component.monitor.WatchDog;
import cn.com.kun.component.monitor.model.MonitorTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorDemoService.class);

    private String bizType = "batch-job";

    @Autowired
    private MonitorDaoMockServcie monitorDaoMockServcie;

    @Autowired
    MonitorManager monitorManager;

    public void doService(String jobId){

        //开启了一个job之后，需要开启一个新的监控任务
        LOGGER.info("执行业务逻辑：jobId = {}", jobId);
        monitorDaoMockServcie.addStatus(jobId, "Running");
        //
        WatchDog.addFlag(jobId, "N");

        LOGGER.info("创建监控任务");
        startMonitorTask(jobId);
    }

    private void startMonitorTask(String jobId){

        MonitorTask monitorTask = new MonitorTask(bizType);
        monitorTask.setMonitorWorker(new MonitorWorker() {

            @Override
            public boolean doMonitor() {

                LOGGER.info("执行监控逻辑,jobId:{}", jobId);
                String status = monitorDaoMockServcie.query(jobId);
                //查库，假如记录状态不需要监控，则返回true
                if ("Done".equals(status) || "Fail".equals(status)){
                    LOGGER.info("检测到任务[{}]已完成或者失败", jobId);
                    return true;
                }

                //假如仍在运行中，继续监控

                if ("Stop".equals(status)){
                    LOGGER.info("检测到任务[{}]已停止", jobId);
                    //假如任务收到了停止标记，监控到停止状态，则返回一个停止标记
                    if (WatchDog.hasFlag(jobId) != null){
                        //说明任务仍在运行中
                        LOGGER.info("设置任务[{}]停止标记", jobId);
                        WatchDog.addFlag(jobId, "Y");
                    }
                    //返回true,表示不需要再监控
                    return true;
                }
                return false;
            }

        });

        monitorManager.addMonitorTask(monitorTask);
    }

}
