package cn.com.kun.foo.javacommon.completableFuture.serialInvoke;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * author:xuyaokun_kzx
 * date:2022/7/6
 * desc:
*/
public class SerialInvokeDemo {

    private UserInfoQueryService userInfoQueryService;
    private BannerService bannerService;
    private LabelService labelService;

    public SerialInvokeDemo(UserInfoQueryService userInfoQueryService, BannerService bannerService, LabelService labelService) {
        this.userInfoQueryService = userInfoQueryService;
        this.bannerService = bannerService;
        this.labelService = labelService;
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        UserInfoQueryService userInfoQueryService = new UserInfoQueryService();
        BannerService bannerService = new BannerService();
        LabelService labelService = new LabelService();
        AppHeadInfoResponse appHeadInfoResponse = new SerialInvokeDemo(userInfoQueryService, bannerService, labelService).queryAppHeadInfo(new AppInfoReq());
        System.out.println(JacksonUtils.toJSONString(appHeadInfoResponse));
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

    public AppHeadInfoResponse queryAppHeadInfo(AppInfoReq req) {

        //查用户信息
        UserInfoParam userInfoParam = buildUserParam(req);
        UserInfoDTO userInfoDTO = userInfoQueryService.queryUserInfo(userInfoParam);
        //查banner信息
        BannerParam bannerParam = buildBannerParam(req);
        BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
        //查标签信息
        LabelParam labelParam = buildLabelParam(req);
        LabelDTO labelDTO = labelService.queryLabelInfo(labelParam);
        //组装结果
        return buildResponse(userInfoDTO, bannerDTO, labelDTO);
    }

    private LabelParam buildLabelParam(AppInfoReq req) {
        return null;
    }

    private BannerParam buildBannerParam(AppInfoReq req) {
        return null;
    }

    private UserInfoParam buildUserParam(AppInfoReq req) {
        return null;
    }

    private AppHeadInfoResponse buildResponse(UserInfoDTO userInfoDTO, BannerDTO bannerDTO, LabelDTO labelDTO) {

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("userInfo", userInfoDTO);
        objMap.put("bannerDTO", bannerDTO);
        objMap.put("labelDTO", labelDTO);
        return new AppHeadInfoResponse(objMap);
    }


}
