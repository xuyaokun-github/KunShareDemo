package cn.com.kun.thread;


import cn.com.kun.common.vo.ResultVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


/**
 * @author Administrator
 *
 */
public class SimpleThreadUtil {

    /**
     * 日志记录
     */
//    private static Logger log = Logger.getLogger(SimpleThreadUtil.class);
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()*3);

    /**
     * 忽略异常
     * @param tasks
     * @return
     */
    public static <V> List<V> executeIgnoreException(Collection<Callable<V>> tasks) {
        if (tasks == null) return null;
        if (tasks.isEmpty()) return Collections.emptyList();
        int num = tasks.size();
        List<V> results = new ArrayList<V>(num);
        CompletionService<V> ecs = new ExecutorCompletionService<V>(cachedThreadPool);
        for (Callable<V> task : tasks) {
            ecs.submit(task);
        }
        for (int i = 0; i < num; i ++) {
            try {
                results.add(ecs.take().get());
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
        return results;
    }

    /**
     * 记录异常日志, 并返回ResultVo
     * @param tasks
     * @return
     */
    public static <V> List<ResultVo> executeWithException(Collection<Callable<V>> tasks) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        if (tasks == null) {
            return null;
        }
        if (tasks.isEmpty()) {
            return Collections.emptyList();
        }
        int num = tasks.size();
        List<ResultVo> results = new ArrayList<ResultVo>(num);
        CompletionService<V> ecs = new ExecutorCompletionService<V>(cachedThreadPool);
        for (Callable<V> task : tasks) {
            ecs.submit(task);
        }
        for (int i = 0; i < num; i++) {
            try {
                results.add((ResultVo) ecs.take().get());
            } catch (InterruptedException e) {
//                log.error("监控线程执行异常", e);
                results.add(ResultVo.valueOfError(e.getMessage()));
            } catch (ExecutionException e) {
//                log.error("监控线程执行异常", e);
                results.add(ResultVo.valueOfError(e.getMessage()));
            }
        }
        cachedThreadPool.shutdown();
        return results;
    }
}

