package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo1;

import cn.com.kun.common.vo.ResultVo;

public class LakersNbaTeamHandler extends NbaTeamHandler {

    public LakersNbaTeamHandler(String handlerName) {
        super(handlerName);
    }

    @Override
    public ResultVo providePlayer(String requirement) {

        if (requirement.contains("lakers")){
            return ResultVo.valueOfSuccess("AD");
        }else {
            return this.successor.providePlayer(requirement);
        }
    }

}
