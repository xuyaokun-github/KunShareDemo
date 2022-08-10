package cn.com.kun.controller.other;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.switchcheck.SwitchCheckerDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/switchChecker")
@RestController
public class SwitchCheckerDemoController {

    @Autowired
    SwitchCheckerDemoService switchCheckerDemoService;

    @GetMapping("/test")
    public ResultVo testExclude(){
        switchCheckerDemoService.test();
        return ResultVo.valueOfSuccess();
    }
}
