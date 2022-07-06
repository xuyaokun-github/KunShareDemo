package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.AppInfoReq;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.UserInfoDTO;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.UserInfoParam;
import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.UserInfoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//用户信息策略类
@Service
public class UserInfoStrategyTask implements IBaseTask {

    @Autowired
    private UserInfoQueryService userInfoQueryService;

    @Override
    public String getTaskType() {
        return "userInfoDTO";
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        UserInfoParam userInfoParam = userInfoQueryService.buildUserParam(req);
        UserInfoDTO userInfoDTO = userInfoQueryService.queryUserInfo(userInfoParam);
        BaseRspDTO<Object> userBaseRspDTO = new BaseRspDTO<Object>();
        userBaseRspDTO.setKey(getTaskType());
        userBaseRspDTO.setData(userInfoDTO);
        return userBaseRspDTO;
    }
}
