package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ParallelQueryAppHeadPageInfoService {

    @Autowired
    TaskStrategyFactory taskStrategyFactory;

    @Autowired
    ParallelInvokeCommonService parallelInvokeCommonService;

    public AppHeadInfoResponse parallelQueryAppHeadPageInfo2(AppInfoReq req) {

        long beginTime = System.currentTimeMillis();
        System.out.println("开始并行查询app首页信息（最终版本），开始时间：" + beginTime);
        List<Callable<BaseRspDTO<Object>>> taskList = new ArrayList<>();
        //用户信息查询任务
        taskList.add(new BaseTaskCommand("userInfoDTO", req, taskStrategyFactory));
        //banner查询任务
        taskList.add(new BaseTaskCommand("bannerDTO", req, taskStrategyFactory));
        //标签查询任务
        taskList.add(new BaseTaskCommand("labelDTO", req, taskStrategyFactory));
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<BaseRspDTO<Object>> resultList = parallelInvokeCommonService.executeTask(taskList, 3, executor);
        if (resultList == null || resultList.size() == 0) {
            return new AppHeadInfoResponse(null);
        }
        UserInfoDTO userInfoDTO = null;
        BannerDTO bannerDTO = null;
        LabelDTO labelDTO = null;
        for (BaseRspDTO<Object> baseRspDTO : resultList) {
            if ("userInfoDTO".equals(baseRspDTO.getKey())) {
                userInfoDTO = (UserInfoDTO) baseRspDTO.getData();
            } else if ("bannerDTO".equals(baseRspDTO.getKey())) {
                bannerDTO = (BannerDTO) baseRspDTO.getData();
            } else if ("labelDTO".equals(baseRspDTO.getKey())) {
                labelDTO = (LabelDTO) baseRspDTO.getData();
            }
        }
        System.out.println("结束并行查询app首页信息（最终版本）,总耗时：" + (System.currentTimeMillis() - beginTime));
        return buildResponse(userInfoDTO, bannerDTO, labelDTO);
    }

    private AppHeadInfoResponse buildResponse(UserInfoDTO userInfoDTO, BannerDTO bannerDTO, LabelDTO labelDTO) {

        Map<String, Object> objMap = new HashMap<>();
        objMap.put("userInfo", userInfoDTO);
        objMap.put("bannerDTO", bannerDTO);
        objMap.put("labelDTO", labelDTO);
        return new AppHeadInfoResponse(objMap);
    }


}
