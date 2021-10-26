package cn.com.kun.springframework.springretry.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springretry.service.SpringRetryDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * author:xuyaokun_kzx
 * date:2021/10/25
 * desc:
*/
@RequestMapping("/spring-retry")
@RestController
public class SpringRetryDemocontroller {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRetryDemocontroller.class);

    @Autowired
    private SpringRetryDemoService springRetryDemoService;

    /**
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/test")
    public ResultVo test(HttpServletRequest request){
        ResultVo resultVo = springRetryDemoService.test1(new String());
        return resultVo;
    }

    @GetMapping(value = "/testStateful")
    public String testStateful(HttpServletRequest request){
        ResultVo resultVo = springRetryDemoService.testStateful(new String());
        return "OK";
    }


}
