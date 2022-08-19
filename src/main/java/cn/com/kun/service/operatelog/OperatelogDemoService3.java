package cn.com.kun.service.operatelog;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.stereotype.Service;

@Service
public class OperatelogDemoService3 {


    //上注解
//    @OperateLog(moduleName = "球员模块", operateType = OperateTypeEnum.UPDATE, selectKey = "#playerDO.playerId",
//            targetName="operatelogDemoService", methodName = "query")
    public ResultVo update(PlayerDO playerDO){

        //做具体的dao层更新，这是业务逻辑，和切面无关

        return ResultVo.valueOfSuccess();
    }

    public PlayerDO query(Long playerId){

        //
        PlayerDO playerDO = new PlayerDO();
        playerDO.setPlayName("old-name");
        playerDO.setPlayAddress("深圳市南山区");
        playerDO.setPlayerId(888L);
        return playerDO;
    }


}
