package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.AppInfoReq;

public interface IBaseTask {
    //返回每个策略类的key，如是usetInfoDTO还是bannerDTO，还是labelDTO
    String getTaskType();
    BaseRspDTO<Object> execute(AppInfoReq req);
}


