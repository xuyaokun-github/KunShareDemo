package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.AppInfoReq;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.BannerDTO;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.BannerParam;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * banner信息策略实现类
 **/
@Service
public class BannerStrategyTask implements IBaseTask {
    @Autowired
    private BannerService bannerService;

    @Override
    public String getTaskType() {
        return "bannerDTO";
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        BannerParam bannerParam = bannerService.buildBannerParam(req);
        BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
        BaseRspDTO<Object> bannerBaseRspDTO = new BaseRspDTO<Object>();
        bannerBaseRspDTO.setKey(getTaskType());
        bannerBaseRspDTO.setData(bannerDTO);
        return bannerBaseRspDTO;
    }
}