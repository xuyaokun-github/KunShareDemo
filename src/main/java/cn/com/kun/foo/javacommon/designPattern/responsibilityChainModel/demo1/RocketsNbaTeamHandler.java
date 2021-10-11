package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo1;

import cn.com.kun.common.vo.ResultVo;

public class RocketsNbaTeamHandler extends NbaTeamHandler {

    public RocketsNbaTeamHandler(String handlerName) {
        super(handlerName);
    }

    @Override
    public ResultVo providePlayer(String requirement) {
        if (requirement.contains("rockets")){
            return ResultVo.valueOfSuccess("Tmac");
        }else {
            //它没有后继了，只能返回错误了
            return ResultVo.valueOfError("找不到符合的球员");
        }
    }

}
