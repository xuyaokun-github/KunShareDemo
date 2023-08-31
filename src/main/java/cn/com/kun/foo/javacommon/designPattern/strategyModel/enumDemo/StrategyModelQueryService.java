package cn.com.kun.foo.javacommon.designPattern.strategyModel.enumDemo;

import cn.com.kun.common.utils.JacksonUtils;

public class StrategyModelQueryService {


    public String query(String channel){

        //调用查询逻辑
        //策略模式应用--根据渠道选择不同的策略,构造不同的请求参数
        UserBindQryTypeEnum userBindQryTypeEnum = UserBindQryTypeEnum.get(channel);
        UserCenterQueryVO userCenterQueryVO = userBindQryTypeEnum.buildQryParams();
        System.out.println("获取到的查询参数" + JacksonUtils.toJSONString(userCenterQueryVO));

        userBindQryTypeEnum.existBindRelation(new UserCenterQueryResVO());

        return "";
    }


}
