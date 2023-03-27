package cn.com.kun.controller.memorycache;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.constants.MemoryCacheConfigConstants;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.memorycache.apply.MemoryCacheCleaner;
import cn.com.kun.service.memorycache.MemoryCacheApplyAppStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 模拟缓存的应用方
 *
 * author:xuyaokun_kzx
 * date:2023/1/19
 * desc:
*/
@RequestMapping("/memory-cache-apply")
@RestController
public class MemoryCacheApplyAppController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheApplyAppController.class);

    @Autowired
    private MemoryCacheApplyAppStudentService memoryCacheDemoStudentService;

    @Autowired
    private MemoryCacheCleaner memoryCacheCleaner;


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
    @GetMapping("/query")
    public ResultVo<StudentResVO> query(Long id){

        StudentReqVO reqVO = new StudentReqVO();
        reqVO.setId(id);
        reqVO.setAddress("shenzhen");
        if (id == null){
            reqVO.setId(10L);
        }
        StudentResVO studentResVO = memoryCacheDemoStudentService.queryStudent(reqVO);
        StudentResVO studentResVO11 = memoryCacheDemoStudentService.queryStudent2(reqVO);
        reqVO.setId(12L);
        reqVO.setAddress("guangzhou");

        StudentResVO studentResVO2 = memoryCacheDemoStudentService.queryStudent(reqVO);
        StudentResVO studentResVO22 = memoryCacheDemoStudentService.queryStudent2(reqVO);

        StudentResVO studentRtnNull = memoryCacheDemoStudentService.queryStudentRtnNull(reqVO);

        return ResultVo.valueOfSuccess(studentResVO);
    }



    /**
     * http://localhost:8080/kunsharedemo/memory-cache/update?id=10
     * @return
     */
    @PostMapping("/update")
    public ResultVo<Integer> update(@RequestBody StudentReqVO reqVO){

        Integer res = memoryCacheDemoStudentService.updateStudent(reqVO);
        return ResultVo.valueOfSuccess(res);
    }

    @GetMapping("/clearAll")
    public ResultVo clearAll(){

        cacheManager.getCacheNames().forEach(cacheName->{
            cacheManager.getCache(cacheName).clear();
        });
        return null;
    }

    @GetMapping("/cacheManager")
    public ResultVo<Integer> cacheManager(){

        CacheManager cacheManager = memoryCacheCleaner.getCacheManager();
        Cache cache = cacheManager.getCache(MemoryCacheConfigConstants.MEMORY_CACHE_CONFIG_NAME_STUDENT_2);
        cache.get("kugnhsu");
        Object object = cache.getNativeCache();

        if (object instanceof com.github.benmanes.caffeine.cache.Cache){
            com.github.benmanes.caffeine.cache.Cache cache1 = (com.github.benmanes.caffeine.cache.Cache) object;
            ConcurrentMap concurrentMap = cache1.asMap();//获取所有缓存内容

            Set set =concurrentMap.keySet();
            set.forEach(item->{
                Object val = concurrentMap.get(item);
                LOGGER.info("key:{} value:{}", item, val);
            });
            concurrentMap.size();
        }

        return ResultVo.valueOfSuccess();
    }
}
