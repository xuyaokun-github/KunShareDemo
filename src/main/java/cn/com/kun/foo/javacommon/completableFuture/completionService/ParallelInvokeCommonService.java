package cn.com.kun.foo.javacommon.completableFuture.completionService;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 通用的并行调用
 * (其实作为一个通用的调用模板，不用定义成bean，定义成普通的类，初始化工作交给固定的服务层完成，因为不同的业务并行使用的是不同的线程池)
 * author:xuyaokun_kzx
 * date:2022/7/6
 * desc:
*/
@Component
public class ParallelInvokeCommonService {

    /**
     * 并行调用，返回所有结果的集合
     *
     * @param taskList
     * @param timeOut
     * @param executor
     * @return
     */
    public List<BaseRspDTO<Object>> executeTask(List<Callable<BaseRspDTO<Object>>> taskList, long timeOut, ExecutorService executor) {

        List<BaseRspDTO<Object>> resultList = new ArrayList<>();
        //校验参数
        if (taskList == null || taskList.size() == 0) {
            return resultList;
        }
        if (executor == null) {
            return resultList;
        }
        if (timeOut <= 0) {
            return resultList;
        }
        // 提交任务
        CompletionService<BaseRspDTO<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDTO<Object>>(executor);
        for (Callable<BaseRspDTO<Object>> task : taskList) {
            baseDTOCompletionService.submit(task);
        }
        try {
            //遍历获取结果
            for (int i = 0; i < taskList.size(); i++) {
                Future<BaseRspDTO<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(timeOut, TimeUnit.SECONDS);
                resultList.add(baseRspDTOFuture.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}