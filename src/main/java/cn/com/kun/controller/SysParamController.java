package cn.com.kun.controller;

import cn.com.kun.bean.model.SysParamReqVO;
import cn.com.kun.bean.model.SysParamResVO;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.PageParam;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 一个Restful风格的接口示例
 *
 * author:xuyaokun_kzx
 * date:2021/8/12
 * desc:
*/
@RequestMapping("/sysparams")
@RestController
public class SysParamController extends BaseController {

    public final static Logger LOGGER = LoggerFactory.getLogger(SysParamController.class);


    /**
     * SELECT 查询操作，返回一个JSON数组
     * 查询条件通过url携带，放在？号后
     * 非分页
     * 具有幂等性
     * */
    @GetMapping
    public ResultVo<List<SysParamResVO>> getSysParams(@RequestParam Map<String, String> reqVO){

        //查询条件转成具体实体类，再传到服务层
        SysParamReqVO sysParamReqVO = buildRequestObj(reqVO, SysParamReqVO.class);
        LOGGER.info(JacksonUtils.toJSONString(sysParamReqVO));
        return ResultVo.valueOfSuccess();
    }

    /**
     * SELECT 查询操作，返回单个资源
     * 具有幂等性
     * */
    @GetMapping("/{paramId}")
    public ResultVo<SysParamResVO> getSysParam(@PathVariable("paramId") String paramId){

        return ResultVo.valueOfSuccess();
    }

    /**
     * 分页查询
     * 假如像我工程中这样设计的分页请求对象，是嵌套的，里面才是具体的业务查询
     * 具体请看cn.com.kun.common.vo.PageParam
     * http://localhost:8080/kunsharedemo/sysparams/v1/page?paramId=QWER
     *
     * @return
     */
    @GetMapping("/v1/page")
    public ResultVo<List<SysParamResVO>> getSysParamByPage(@RequestParam Map<String, String> reqVO){

        PageParam<SysParamReqVO> pageParam = buildPageParam(reqVO, SysParamReqVO.class);
        //业务层要的是PageParam类
        LOGGER.info("传给业务层的PageParam：{}", JacksonUtils.toJSONString(pageParam));

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
    @DeleteMapping("/v1/{paramId}")
    public ResultVo<Integer> delete(@PathVariable("paramId") String paramId){

        return ResultVo.valueOfSuccess();
    }

    /**
     * 删除一个用户对象
     * 幂等性
     * 删除方法也可以使用@RequestBody,通过body传参
     * */
    @DeleteMapping("/v2/{paramId}")
    public ResultVo<Integer> delete(@PathVariable("paramId") String paramId, @RequestBody SysParamReqVO reqVO){

        LOGGER.info(JacksonUtils.toJSONString(reqVO));
        return ResultVo.valueOfSuccess();
    }

    /**
     * 通过url传参
     * 例子：http://localhost:8080/kunsharedemo/sysparams/v3/QWER?id=2&paramId=QWER&paramValue=xxxxxxx
     * 其实这里的动态url {paramId}也可以放到？后，效果是一样的
     * @param paramId
     * @param reqVO
     * @return
     */
    @DeleteMapping("/v3/{paramId}")
    public ResultVo<Integer> delete(@PathVariable("paramId") String paramId, @RequestParam Map<String, String> reqVO){

        LOGGER.info(JacksonUtils.toJSONString(reqVO));
        //将url后的参数，转成具体的实体类，这个过程可以通过BaseController完成
//        SysParamReqVO sysParamReqVO = JacksonUtils.toJavaObject(reqVO, SysParamReqVO.class);
        SysParamReqVO sysParamReqVO = buildRequestObj(reqVO, SysParamReqVO.class);
        LOGGER.info(JacksonUtils.toJSONString(sysParamReqVO));
        return ResultVo.valueOfSuccess();
    }

}
