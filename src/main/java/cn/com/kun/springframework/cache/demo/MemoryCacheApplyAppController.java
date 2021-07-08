package cn.com.kun.springframework.cache.demo;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/memory-cache")
@RestController
public class MemoryCacheApplyAppController {

    public final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheApplyAppController.class);

    @Autowired
    private MemoryCacheApplyAppStudentService memoryCacheDemoStudentService;

    /**
     * 拿到缓存管理器，它可以操作所有缓存对象
     */
    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;

    /**
     * 查询缓存
     * http://localhost:8080/kunsharedemo/memory-cache/query?id=10
     * @param id
     * @return
     */
    @RequestMapping("/query")
    public ResultVo<StudentResVO> query(Long id){

        StudentReqVO reqVO = new StudentReqVO();
        reqVO.setId(id);
        StudentResVO studentResVO = memoryCacheDemoStudentService.queryStudent(reqVO);
        return ResultVo.valueOfSuccess(studentResVO);
    }

    /**
     * http://localhost:8080/kunsharedemo/memory-cache/update?id=10
     * @return
     */
    @RequestMapping("/update")
    public ResultVo<Integer> update(@RequestBody StudentReqVO reqVO){

        Integer res = memoryCacheDemoStudentService.updateStudent(reqVO);
        return ResultVo.valueOfSuccess(res);
    }
}
