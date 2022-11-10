package cn.com.kun.component.monitor.executor;

import cn.com.kun.component.monitor.MonitorManager;
import cn.com.kun.component.monitor.model.MonitorTask;
import cn.com.kun.component.monitor.worker.MonitorWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 监控组件执行器
 *
 * author:xuyaokun_kzx
 * date:2022/11/4
 * desc:
*/
public class MonitorExecutor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorManager.class);

    private static ExecutorService executor = null;

    /**
     * 监控任务队列
     */
    private static ConcurrentLinkedQueue taskQueue = new ConcurrentLinkedQueue();

    /**
     * 已提交运行的任务的Future集合
     */
    private static Map<String, Future> futureMap = new ConcurrentHashMap<>();

    /**
     * 监控逻辑集合
     */
    private static Map<String, MonitorWorker> monitorWorkerMap = new ConcurrentHashMap<>();

    public static void startExecutor() {

        //1.启动一个线程，负责扫描队列
        new Thread(()->{

            while (true){
                try {
                    //每隔一秒钟，判断一次队列里是否有元素
                    Thread.sleep(1000);
                    List<MonitorTask> reAddMonitorTasks = null;
                    //有的话就往线程池里丢
                    while (!taskQueue.isEmpty()){
                        MonitorTask monitorTask = (MonitorTask) taskQueue.poll();
                        if (checkExectime(monitorTask)){
                            Assert.notNull(executor, "监控任务执行器不能为空");
                            //封装成监控任务
                            MonitorTaskRunnable monitorTaskRunnable = new MonitorTaskRunnable(monitorTask);
                            Future future = executor.submit(monitorTaskRunnable);
                            //记录执行时间
                            monitorTask.setLastExecTimeMillis(System.currentTimeMillis());
                            futureMap.put(monitorTask.getMonitorTaskId(), future);
                        }else {
                            //执行时间未到，重新进队
                            if (reAddMonitorTasks == null){
                                reAddMonitorTasks = new ArrayList<>();
                            }
                            reAddMonitorTasks.add(monitorTask);
                        }
                    }

                    if (!CollectionUtils.isEmpty(reAddMonitorTasks)){
                        reAddMonitorTasks.forEach(e ->{
                            taskQueue.offer(e);
                        });
                    }
                }catch (Exception e){
                    LOGGER.error("scan monitor task error", e);
                }
            }

        }, "ScanMonitorTask-Thread").start();

    }

    private static boolean checkExectime(MonitorTask monitorTask) {

        if (monitorTask.getLastExecTimeMillis() > 0){
            //说明已经执行过，判断时间间隔，是否可以开始下一次执行
            return (System.currentTimeMillis() - monitorTask.getTimeUnit().toMillis(monitorTask.getTimePeriod()))
                    > monitorTask.getLastExecTimeMillis();
        }

        return true;
    }

    /**
     * 注册执行器
     * 可根据业务量大小，自由扩展
     */
    public static void setExecutor(ExecutorService executorService){
        if (executor != null){
            return;
        }
        executor = executorService;
    }

    public static void offerTask(MonitorTask monitorTask) {
        taskQueue.offer(monitorTask);
    }

    public static void registerMonitorWorker(String bizType, MonitorWorker monitorWorker) {
        monitorWorkerMap.put(bizType, monitorWorker);
    }

    private static void removeTask(String monitorTaskId) {

        //移除任务
        taskQueue.removeIf(task -> monitorTaskId.equals(task));
    }

    public static void cancelMonitorTask(String monitorTaskId) {

        //如何取消，调Future的取消方法
        //每一个监控任务都有它的唯一ID，根据ID取消任务执行
        Future future = MonitorExecutor.getFuture(monitorTaskId);
        if (future != null){
            //取消
            future.cancel(true);
            removeFuture(monitorTaskId);
        }else {
            //假如任务只是放入了taskQueue，还没进线程池，应该从队列中移除
            removeTask(monitorTaskId);
        }
    }

    private static class MonitorTaskRunnable implements Runnable{

        private MonitorWorker monitorWorker;

        private MonitorTask monitorTask;

        public MonitorTaskRunnable(MonitorTask monitorTask) {
            this.monitorTask = monitorTask;
            this.monitorWorker = getMonitorWorker(monitorTask);
        }

        private MonitorWorker getMonitorWorker(MonitorTask monitorTask) {

            MonitorWorker monitorWorker;
            if (monitorTask.getMonitorWorker() != null){
                //有些监控任务是和参数有关的，则由用户自由定义
                monitorWorker = monitorTask.getMonitorWorker();
            }else {
                monitorWorker = monitorWorkerMap.get(monitorTask.getBizType());
            }
            Assert.notNull(monitorWorker, "MonitorWorker不能为空");
            return monitorWorker;
        }

        @Override
        public void run() {
            boolean finishFlag = false;
            try {
                //执行监控逻辑
                boolean flag = monitorWorker.doMonitor();
                //假如监控未完成，继续入队列，等待下一次监控
                if (!flag){
                    reAddToQueue(monitorTask);
                }else {
                    //假如监控已经完成，无需再次进队，清除futureMap
                    removeFuture(monitorTask.getMonitorTaskId());
                }
                finishFlag = true;
            }catch (Exception e){
                LOGGER.error(String.format("run monitor task error, bizType:%s", monitorTask.getBizType()), e);
            }finally {
                if (!finishFlag){
                    //假如出异常，必须再次进队，否则需要永久检测的任务将停止（开发人员可能会遗漏异常捕获）
                    reAddToQueue(monitorTask);
                }
            }
        }

        private void reAddToQueue(MonitorTask monitorTask) {
            taskQueue.offer(monitorTask);
        }

    }

    private static void removeFuture(String monitorTaskId) {
        futureMap.remove(monitorTaskId);
    }

    private static Future getFuture(String monitorTaskId) {

        //从map里获取Future
        return futureMap.get(monitorTaskId);
    }
}
