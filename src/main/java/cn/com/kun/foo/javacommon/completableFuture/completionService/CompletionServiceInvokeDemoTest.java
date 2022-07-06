package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.*;

public class CompletionServiceInvokeDemoTest {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        UserInfoQueryService userInfoQueryService = new UserInfoQueryService();
        BannerService bannerService = new BannerService();
        LabelService labelService = new LabelService();
        AppHeadInfoResponse appHeadInfoResponse = new CompletionServiceInvokeDemo(userInfoQueryService, bannerService, labelService)
//                .parallelQueryAppHeadPageInfo(new AppInfoReq());
                    .parallelQueryAppHeadPageInfo1(new AppInfoReq());
        System.out.println(JacksonUtils.toJSONString(appHeadInfoResponse));
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }
}
