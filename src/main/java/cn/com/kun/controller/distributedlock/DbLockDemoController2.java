package cn.com.kun.controller.distributedlock;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.distributedlock.dblock.DBLockDemoService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dblock-demo2")
@RestController
public class DbLockDemoController2 {

    @Autowired
    private DBLockDemoService2 dbLockDemoService2;

    @GetMapping("/testRunFastJob")
    public ResultVo<String> testRunFastJob(){

        try {
            dbLockDemoService2.test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResultVo.valueOfSuccess("");
    }


}
