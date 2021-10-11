package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo1;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;

public class TestNbaTeamHandler {


    public static void main(String[] args) {

        NbaTeamHandler nbaTeamHandler = NbaTeamHandlerFactory.createNbaTeamHandler();
        ResultVo resultVo = nbaTeamHandler.providePlayer("I want player from rockets！");
        System.out.println(JacksonUtils.toJSONString(resultVo));
        ResultVo resultVo2 = nbaTeamHandler.providePlayer("I want player from xinjiang！");
        System.out.println(JacksonUtils.toJSONString(resultVo2));
        ResultVo resultVo3 = nbaTeamHandler.providePlayer("I want player from lakers！");
        System.out.println(JacksonUtils.toJSONString(resultVo3));
    }
}
