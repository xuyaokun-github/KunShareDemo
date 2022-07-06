package cn.com.kun.foo.javacommon.completableFuture.completionService;

import cn.com.kun.foo.javacommon.completableFuture.serialInvoke.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 并行调用例子
 * 还能继续优化，还不是最终版
 *
 * author:xuyaokun_kzx
 * date:2022/7/6
 * desc:
 */
public class CompletionServiceInvokeDemo {

    private UserInfoQueryService userInfoQueryService;
    private BannerService bannerService;
    private LabelService labelService;

    private ParallelInvokeCommonService parallelInvokeCommonService = new ParallelInvokeCommonService();

    public CompletionServiceInvokeDemo(UserInfoQueryService userInfoQueryService, BannerService bannerService, LabelService labelService) {
        this.userInfoQueryService = userInfoQueryService;
        this.bannerService = bannerService;
        this.labelService = labelService;
    }

    //并行查询App首页信息
    public AppHeadInfoResponse parallelQueryAppHeadPageInfo(AppInfoReq req) {

        long beginTime = System.currentTimeMillis();
        System.out.println("开始并行查询app首页信息，开始时间：" + beginTime);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<BaseRspDTO<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDTO<Object>>(executor);
        //查询用户信息任务
        Callable<BaseRspDTO<Object>> userInfoDTOCallableTask = () -> {
            UserInfoParam userInfoParam = buildUserParam(req);
            UserInfoDTO userInfoDTO = userInfoQueryService.queryUserInfo(userInfoParam);
            BaseRspDTO<Object> userBaseRspDTO = new BaseRspDTO<Object>();
            userBaseRspDTO.setKey("userInfoDTO");
            userBaseRspDTO.setData(userInfoDTO);
            return userBaseRspDTO;
        };
        //banner信息查询任务
        Callable<BaseRspDTO<Object>> bannerDTOCallableTask = () -> {
            BannerParam bannerParam = buildBannerParam(req);
            BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
            BaseRspDTO<Object> bannerBaseRspDTO = new BaseRspDTO<Object>();
            bannerBaseRspDTO.setKey("bannerDTO");
            bannerBaseRspDTO.setData(bannerDTO);
            return bannerBaseRspDTO;
        };
        //label信息查询任务
        Callable<BaseRspDTO<Object>> labelDTODTOCallableTask = () -> {
            LabelParam labelParam = buildLabelParam(req);
            LabelDTO labelDTO = labelService.queryLabelInfo(labelParam);
            BaseRspDTO<Object> labelBaseRspDTO = new BaseRspDTO<Object>();
            labelBaseRspDTO.setKey("labelDTO");
            labelBaseRspDTO.setData(labelDTO);
            return labelBaseRspDTO;
        };
        //提交用户信息任务
        baseDTOCompletionService.submit(userInfoDTOCallableTask);
        //提交banner信息任务
        baseDTOCompletionService.submit(bannerDTOCallableTask);
        //提交label信息任务
        baseDTOCompletionService.submit(labelDTODTOCallableTask);
        UserInfoDTO userInfoDTO = null;
        BannerDTO bannerDTO = null;
        LabelDTO labelDTO = null;
        try {
            //因为提交了3个任务，所以获取结果次数是3
            for (int i = 0; i < 3; i++) {
                //注意这里的poll时间，假如超过了这个时间，这里将会结束，拿到空！
                //所以这里最好还是加一个非空判断
//                Future<BaseRspDTO<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(1, TimeUnit.SECONDS);
                Future<BaseRspDTO<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(5, TimeUnit.SECONDS);
                BaseRspDTO baseRspDTO = baseRspDTOFuture.get();
                if ("userInfoDTO".equals(baseRspDTO.getKey())) {
                    userInfoDTO = (UserInfoDTO) baseRspDTO.getData();
                } else if ("bannerDTO".equals(baseRspDTO.getKey())) {
                    bannerDTO = (BannerDTO) baseRspDTO.getData();
                } else if ("labelDTO".equals(baseRspDTO.getKey())) {
                    labelDTO = (LabelDTO) baseRspDTO.getData();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("结束并行查询app首页信息,总耗时：" + (System.currentTimeMillis() - beginTime));
        return buildResponse(userInfoDTO, bannerDTO, labelDTO);
    }


    public AppHeadInfoResponse parallelQueryAppHeadPageInfo1(AppInfoReq req) {

        long beginTime = System.currentTimeMillis();
        System.out.println("开始并行查询app首页信息，开始时间：" + beginTime);

        //用户信息查询任务
        Callable<BaseRspDTO<Object>> userInfoDTOCallableTask = () -> {
            UserInfoParam userInfoParam = buildUserParam(req);
            UserInfoDTO userInfoDTO = userInfoQueryService.queryUserInfo(userInfoParam);
            BaseRspDTO<Object> userBaseRspDTO = new BaseRspDTO<Object>();
            userBaseRspDTO.setKey("userInfoDTO");
            userBaseRspDTO.setData(userInfoDTO);
            return userBaseRspDTO;
        };
        // banner信息查询任务
        Callable<BaseRspDTO<Object>> bannerDTOCallableTask = () -> {
            BannerParam bannerParam = buildBannerParam(req);
            BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
            BaseRspDTO<Object> bannerBaseRspDTO = new BaseRspDTO<Object>();
            bannerBaseRspDTO.setKey("bannerDTO");
            bannerBaseRspDTO.setData(bannerDTO);
            return bannerBaseRspDTO;
        };
        // label信息查询任务
        Callable<BaseRspDTO<Object>> labelDTODTOCallableTask = () -> {
            LabelParam labelParam = buildLabelParam(req);
            LabelDTO labelDTO = labelService.queryLabelInfo(labelParam);
            BaseRspDTO<Object> labelBaseRspDTO = new BaseRspDTO<Object>();
            labelBaseRspDTO.setKey("labelDTO");
            labelBaseRspDTO.setData(labelDTO);
            return labelBaseRspDTO;
        };
        List<Callable<BaseRspDTO<Object>>> taskList = new ArrayList<>();
        taskList.add(userInfoDTOCallableTask);
        taskList.add(bannerDTOCallableTask);
        taskList.add(labelDTODTOCallableTask);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<BaseRspDTO<Object>> resultList = parallelInvokeCommonService.executeTask(taskList, 3, executor);
        if (resultList == null || resultList.size() == 0) {
            return new AppHeadInfoResponse(null);
        }
        UserInfoDTO userInfoDTO = null;
        BannerDTO bannerDTO = null;
        LabelDTO labelDTO = null;
        //遍历结果    for (int i = 0; i < resultList.size(); i++) {        BaseRspDTO baseRspDTO = resultList.get(i);        if ("userInfoDTO".equals(baseRspDTO.getKey())) {            userInfoDTO = (UserInfoDTO) baseRspDTO.getData();        } else if ("bannerDTO".equals(baseRspDTO.getKey())) {            bannerDTO = (BannerDTO) baseRspDTO.getData();        } else if ("labelDTO".equals(baseRspDTO.getKey())) {            labelDTO = (LabelDTO) baseRspDTO.getData();        }    }
        System.out.println("结束并行查询app首页信息,总耗时：" + (System.currentTimeMillis() - beginTime));
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
