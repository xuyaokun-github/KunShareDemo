package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.*;

import java.util.concurrent.Callable;

public class BaseTaskCommand2 implements Callable<BaseRspDTO<Object>> {

    private String key;
    private AppInfoReq req;
    private UserInfoQueryService userInfoQueryService;
    private BannerService bannerService;
    private LabelService labelService;

    public BaseTaskCommand2(String key, AppInfoReq req, UserInfoQueryService userInfoQueryService, BannerService bannerService, LabelService labelService) {
        this.key = key;
        this.req = req;
        this.userInfoQueryService = userInfoQueryService;
        this.bannerService = bannerService;
        this.labelService = labelService;
    }

    @Override
    public BaseRspDTO<Object> call() throws Exception {
        if ("userInfoDTO".equals(key)) {
            UserInfoParam userInfoParam = buildUserParam(req);
            UserInfoDTO userInfoDTO = userInfoQueryService.queryUserInfo(userInfoParam);
            BaseRspDTO<Object> userBaseRspDTO = new BaseRspDTO<Object>();
            userBaseRspDTO.setKey("userInfoDTO");
            userBaseRspDTO.setData(userInfoDTO);
            return userBaseRspDTO;
        } else if ("bannerDTO".equals(key)) {
            BannerParam bannerParam = buildBannerParam(req);
            BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
            BaseRspDTO<Object> bannerBaseRspDTO = new BaseRspDTO<Object>();
            bannerBaseRspDTO.setKey("bannerDTO");
            bannerBaseRspDTO.setData(bannerDTO);
            return bannerBaseRspDTO;
        } else if ("labelDTO".equals(key)) {
            LabelParam labelParam = buildLabelParam(req);
            LabelDTO labelDTO = labelService.queryLabelInfo(labelParam);
            BaseRspDTO<Object> labelBaseRspDTO = new BaseRspDTO<Object>();
            labelBaseRspDTO.setKey("labelDTO");
            labelBaseRspDTO.setData(labelDTO);
            return labelBaseRspDTO;
        }
        return null;
    }

    private UserInfoParam buildUserParam(AppInfoReq req) {
        return new UserInfoParam();
    }

    private BannerParam buildBannerParam(AppInfoReq req) {
        return new BannerParam();
    }

    private LabelParam buildLabelParam(AppInfoReq req) {
        return new LabelParam();
    }
}
