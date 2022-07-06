package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * banner信息策略实现类
 **/
@Service
public class LabelStrategyTask implements IBaseTask {
    @Autowired
    private LabelService labelService;

    @Override
    public String getTaskType() {
        return "labelDTO";
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        LabelParam bannerParam = labelService.buildLabelParam(req);
        LabelDTO bannerDTO = labelService.queryLabelInfo(bannerParam);
        BaseRspDTO<Object> bannerBaseRspDTO = new BaseRspDTO<Object>();
        bannerBaseRspDTO.setKey(getTaskType());
        bannerBaseRspDTO.setData(bannerDTO);
        return bannerBaseRspDTO;
    }
}