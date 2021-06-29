package cn.com.kun.springframework.cache.demo;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static cn.com.kun.common.constants.MemoryCacheConfigConstants.MEMORY_CACHE_CONFIG_NAME_STUDENT;


@Service
public class MemoryCacheDemoStudentService {

    public final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheDemoStudentService.class);

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;

    @Cacheable(value = MEMORY_CACHE_CONFIG_NAME_STUDENT, key = "#reqVO.id.toString()", cacheManager = "caffeineCacheManager")
    public StudentResVO queryStudent(StudentReqVO reqVO) {

        /**
         * 具体的获取逻辑可以是查库，可以是调接口，可以是查redis
         * 无论是哪种方式，最终的作用就是取数
         *
         * 具体情况分析
         * 1.调接口取数
         * 数据缓存在本服务的内存里，数据更新或者删除是由其他服务方来触发，但缓存的刷新还是需要由本服务完成
         * 所以在本服务要提供一个入口，刷新内存缓存
         * 这个入口可以是基于redis广播刷新，也可以是异步判断时间戳进行更新！
         */
        LOGGER.info("开始调接口获取Student信息");
        StudentResVO studentResVO = new StudentResVO();
        studentResVO.setStudentName("tmac");
        return studentResVO;
    }

    /**
     * 本服务可能是多节点，各个节点被触发广播，主动清空自己的系统内存
     * (不需要每个服务层都写这个方法，用一个统一的类负责清空
     * 这个方法只是提供一个清缓存的思路)
     * @param reqVO
     * @return
     */
    public Integer clearCache(StudentReqVO reqVO) {

        LOGGER.info("开始刷新本服务的内存缓存");
        /**
         * 假如为了快速处理，效率低一点
         */
        cacheManager.getCache(MEMORY_CACHE_CONFIG_NAME_STUDENT).clear();
        /**
         * 需要做到高效，根据具体某个key来删除
         */
        cacheManager.getCache(MEMORY_CACHE_CONFIG_NAME_STUDENT).evict(reqVO.getId().toString());

        return 1;
    }

    /**
     * 本服务主动清除内存h缓存
     * @param reqVO
     * @return
     */
    @CacheEvict(value = MEMORY_CACHE_CONFIG_NAME_STUDENT, key = "#reqVO.id.toString()", beforeInvocation=true, cacheManager = "caffeineCacheManager")
    public Integer updateStudent(StudentReqVO reqVO) {

        /**
         */
        LOGGER.info("开始调接口更新Student信息");
        StudentResVO studentResVO = new StudentResVO();
        studentResVO.setStudentName("tmac");
        return 1;
    }


}
