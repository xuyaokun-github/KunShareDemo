package cn.com.kun.springframework.cache.demo;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这个用来触发 远程接口的数据变更
 * 数据变更发生时需要清空缓存
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@RequestMapping("/memory-other")
@RestController
public class MemCacheOtherAppController {

    /**
     * cglib代理
     */
    @Autowired
    MemCacheOtherAppService memCacheOtherAppService;

    /**
     * JDK代理
     */
    @Autowired
    IMemCacheDemoService memCacheDemoService;

    /**
     * http://localhost:8080/kunsharedemo/memory-other/update?id=10
     * 修改ID为10的学生信息
     * @return
     */
    @RequestMapping("/update")
    public ResultVo<Integer> update(Long id){

        StudentReqVO reqVO = new StudentReqVO();
        reqVO.setId(id);
        Integer res = memCacheDemoService.updateStudent(reqVO);
        return ResultVo.valueOfSuccess(res);
    }

}
