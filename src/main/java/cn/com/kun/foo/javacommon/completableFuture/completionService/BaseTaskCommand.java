package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.AppInfoReq;

import java.util.concurrent.Callable;

public class BaseTaskCommand implements Callable<BaseRspDTO<Object>> {
    private String key;
    private AppInfoReq req;
    private TaskStrategyFactory taskStrategyFactory;

    public BaseTaskCommand(String key, AppInfoReq req, TaskStrategyFactory taskStrategyFactory) {
        this.key = key;
        this.req = req;
        this.taskStrategyFactory = taskStrategyFactory;
    }

    @Override
    public BaseRspDTO<Object> call() throws Exception {
        return taskStrategyFactory.executeTask(key, req);
    }
}
