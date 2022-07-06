package cn.com.kun.foo.javacommon.completableFuture.serialInvoke;

import cn.com.kun.common.utils.ThreadUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserInfoQueryService {


    public UserInfoDTO queryUserInfo(UserInfoParam userInfoParam) {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUsername(UUID.randomUUID().toString());
        ThreadUtils.sleep(1500);
        return userInfoDTO;
    }


    public UserInfoParam buildUserParam(AppInfoReq req) {
        return null;
    }
}
