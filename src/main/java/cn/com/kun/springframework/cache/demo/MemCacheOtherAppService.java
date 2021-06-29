package cn.com.kun.springframework.cache.demo;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.component.memorycache.maintain.EvictCacheNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MemCacheOtherAppService {

    public final static Logger LOGGER = LoggerFactory.getLogger(MemCacheOtherAppService.class);

    /**
     * 改成注解进行刷新处理
     *
     * @param reqVO
     * @return
     */
    @EvictCacheNotice(configName = "memorycache-student-service", key = "#reqVO.id.toString()")
    public Integer updateStudent(StudentReqVO reqVO) {

        LOGGER.info("我是远程其他服务，开始更新学生信息");
        return 1;
    }

}
