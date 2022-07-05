package cn.com.kun.controller.operatelog;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.operatelog.OperatelogDemoService;
import cn.com.kun.service.operatelog.PlayerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperateLogController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OperateLogController.class);

    @Autowired
    private OperatelogDemoService operatelogDemoService;


    @GetMapping("/test-update")
    public ResultVo testUpdate(){

        LOGGER.info("开始做内管更新操作");
        //组装一个新的PlayerDO
        PlayerDO playerDO = new PlayerDO();
        playerDO.setPlayerId(888L);
        playerDO.setPlayName("new-name");
        playerDO.setPlayAddress("深圳市福田区");
        operatelogDemoService.update(playerDO);

        return ResultVo.valueOfSuccess();
    }


}
