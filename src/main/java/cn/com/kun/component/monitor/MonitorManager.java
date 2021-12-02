package cn.com.kun.component.monitor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 *
 * author:xuyaokun_kzx
 * date:2021/11/29
 * desc:
*/
public class MonitorManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorManager.class);

    private static ExecutorService executor = null;

    /**
     * 已提交运行的任务的Future集合
     */
    private static Map<String, Future> futureMap = new ConcurrentHashMap<>();

    /**
     * 监控逻辑集合
     */
    private static Map<String, MonitorWorker> monitorWorkerMap = new ConcurrentHashMap<>();

    private static ConcurrentLinkedQueue taskQueue = new ConcurrentLinkedQueue();

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

            try {
                //执行监控逻辑
                boolean flag = monitorWorker.doMonitor();

                //假如监控未完成，继续入队列，等待下一次监控
                if (!flag){
                    reAddToQueue(monitorTask);
                }else {
                    //假如监控已经完成，清除futureMap
                    removeFuture(monitorTask.getMonitorTaskId());
                }
            }catch (Exception e){
                LOGGER.error(String.format("run monitor task error, bizType:%s", monitorTask.getBizType()), e);
            }
        }

        private void reAddToQueue(MonitorTask monitorTask) {
            taskQueue.offer(monitorTask);
        }

    }

    /**
     * 监控组件初始化
     */
    public static void init(){

        //1.启动一个线程，负责扫描队列
        new Thread(()->{

            while (true){
                try {
                    //每隔一秒钟，就判断一次队列里是否扔有元素
                    Thread.sleep(1000);
                    //有的话就往线程池里丢
                    while (!taskQueue.isEmpty()){
                        MonitorTask monitorTask = (MonitorTask) taskQueue.poll();
                        Assert.notNull(executor, "监控任务执行器不能为空");
                        //封装成监控任务
                        MonitorTaskRunnable monitorTaskRunnable = new MonitorTaskRunnable(monitorTask);
                        Future future = executor.submit(monitorTaskRunnable);
                        futureMap.put(monitorTask.getMonitorTaskId(), future);
                    }

                }catch (Exception e){
                    LOGGER.error("scan monitor task error", e);
                }
            }

        }, "ScanMonitorTask-Thread").start();
    }

    //添加监控任务
    public static String addMonitorTask(MonitorTask monitorTask){


        //监控任务放入队列
        if (StringUtils.isEmpty(monitorTask.getMonitorTaskId())){
            //生成监控任务ID
            String monitorTaskId = UUID.randomUUID().toString();
            monitorTask.setMonitorTaskId(monitorTaskId);
        }

        taskQueue.offer(monitorTask);

        //返回监控任务ID
        return monitorTask.getMonitorTaskId();
    }

    /**
     * 假如监控逻辑是不依赖参数的，可以用这种方法注册
     * 只需注册一次，简化编程时的调用
     * @param bizType
     * @param monitorWorker
     */
    public static void registerMonitorWorker(String bizType, MonitorWorker monitorWorker){
        monitorWorkerMap.put(bizType, monitorWorker);
    }

    /**
     * 注册执行器
     * 可根据业务量大小，自由扩展
     */
    public static void setExecutor(ExecutorService executorService){
        executor = executorService;
    }

    /**
     * 这个方法有没有必要呢？取消已经放入线程池里的job
     * @param monitorTaskId
     */
    public static void cancel(String monitorTaskId){

        //取消监控，如何取消，调Future的取消方法
        //怎么知道该取消哪一个，每一个监控任务都有它的唯一ID
        Future future = getFuture(monitorTaskId);
        if (future != null){
            //取消
            future.cancel(true);
            removeFuture(monitorTaskId);
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
