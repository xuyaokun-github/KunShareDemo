package cn.com.kun.controller.redo;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.redo.RedoDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/redo-demo")
@RestController
public class RedoDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedoDemoController.class);

    @Autowired
    RedoDemoService redoDemoService;

    @GetMapping("/test")
    public ResultVo<String> test(){

        redoDemoService.test1();
        return ResultVo.valueOfSuccess("");
    }



}
