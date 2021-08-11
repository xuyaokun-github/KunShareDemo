package cn.com.kun.controller;

import cn.com.kun.bean.model.SysParamReqVO;
import cn.com.kun.bean.model.SysParamResVO;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sysparams")
@RestController
public class SysParamController {

    public final static Logger LOGGER = LoggerFactory.getLogger(SysParamController.class);


    /**
     * SELECT 查询操作，返回一个JSON数组
     * 具有幂等性
     * */
    @GetMapping
    public ResultVo<List<SysParamResVO>> getSysParams(){


        return ResultVo.valueOfSuccess();
    }

    /**
     * SELECT 查询操作，返回一个新建的JSON对象
     * 具有幂等性
     * */
    @GetMapping("/{paramId}")
    public ResultVo<SysParamResVO> getSysParam(@PathVariable("paramId") String paramId){

        return ResultVo.valueOfSuccess();
    }

    /**
     * 新增一个用户对象
     * 非幂等
     * */
    @PostMapping
    public ResultVo<Integer> add(@RequestBody SysParamReqVO reqVO){

        return ResultVo.valueOfSuccess();
    }

    /**
     * 编辑一个用户对象
     * 幂等性
     * */
    @PutMapping
    public ResultVo<Integer> update(@RequestBody SysParamReqVO reqVO){

        return ResultVo.valueOfSuccess();
    }

    /**
     * 删除一个用户对象
     * 幂等性
     * */
    @DeleteMapping
    public ResultVo<Integer> delete(@PathVariable("paramId") String paramId){

        return ResultVo.valueOfSuccess();
    }


}
