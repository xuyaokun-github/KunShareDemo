package cn.com.kun.foo.javacommon.completableFuture.serialInvoke;

import cn.com.kun.common.utils.ThreadUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class BannerService {

    public BannerDTO queryBannerInfo(BannerParam bannerParam) {

        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setBannerName(UUID.randomUUID().toString());
        ThreadUtils.sleep(1500);
        return bannerDTO;
    }

    public BannerParam buildBannerParam(AppInfoReq req) {
        return null;
    }
}
