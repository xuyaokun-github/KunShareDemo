package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo1;

import cn.com.kun.common.vo.ResultVo;

public class HeatsNbaTeamHandler extends NbaTeamHandler {

    public HeatsNbaTeamHandler(String handlerName) {
        super(handlerName);
    }

    @Override
    public ResultVo providePlayer(String requirement) {
        if (requirement.contains("heats")){
            return ResultVo.valueOfSuccess("LBJ");
        }else {
            return this.successor.providePlayer(requirement);
        }
    }

}
