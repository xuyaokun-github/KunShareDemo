package cn.com.kun.springframework.jpa.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.jpa.service.SpringJpaDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/10/17
 * desc:
*/
@RequestMapping("/spring-jpa")
@RestController
public class SpringJpaDemocontroller {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringJpaDemocontroller.class);

    @Autowired
    private SpringJpaDemoService springJpaDemoService;

    /**
     * 增删查改
     * @return
     */
    @GetMapping(value = "/testSave")
    public ResultVo testSave(){

        ResultVo resultVo = springJpaDemoService.testSave(new String());
        return resultVo;
    }

    /**
     * 增删查改
     * @return
     */
    @GetMapping(value = "/testDateNull")
    public ResultVo testDateNull(){

        ResultVo resultVo = springJpaDemoService.testDateNull(new String());
        return resultVo;
    }

    /**
     * 增删查改
     * @return
     */
    @GetMapping(value = "/testQuery")
    public ResultVo testQuery(){

        ResultVo resultVo = springJpaDemoService.testQuery(new String());
        return resultVo;
    }


    /**
     * 增删查改
     * @return
     */
    @GetMapping(value = "/testQueryByWhere")
    public ResultVo testQueryByWhere(){

        ResultVo resultVo = springJpaDemoService.testQueryByWhere(new String());
        return resultVo;
    }

    /**
     * 增删查改
     * @return
     */
    @GetMapping(value = "/testUpdate")
    public ResultVo testUpdate(){

        ResultVo resultVo = springJpaDemoService.testUpdate(new String());
        return resultVo;
    }

    /**
     * 增删查改
     * @return
     */
    @GetMapping(value = "/testDelete")
    public ResultVo testDelete(){

        ResultVo resultVo = springJpaDemoService.testDelete(new String());
        return resultVo;
    }
}
