package cn.com.kun.service.memorycache;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.component.memorycache.maintain.MemoryCacheNoticeProcessor;
import cn.com.kun.component.memorycache.maintain.annotation.EvictCacheNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * memorycache组件使用方例子
 * 使用方法展示
 *
 * author:xuyaokun_kzx
 * date:2023/3/3
 * desc:
*/
@Service
public class MemCacheDemoServiceImpl implements IMemCacheDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemCacheDemoServiceImpl.class);

    @Autowired
    private MemoryCacheNoticeProcessor memoryCacheNoticeProcessor;

    /**
     * 方式1，基于注解
     *
     * @param reqVO
     * @return
     */
    @EvictCacheNotice(configName = "memorycache-student-service", key = "#reqVO.id.toString()")
    @Override
    public Integer updateStudent(StudentReqVO reqVO) {

        //业务逻辑
        LOGGER.info("开始更新学生信息");
        return 1;
    }


    @Override
    public Integer updateStudent2(StudentReqVO reqVO) {

        //业务逻辑
        LOGGER.info("开始更新学生信息");
        memoryCacheNoticeProcessor.notice("memorycache-student-service", reqVO.getId().toString());
        return 1;
    }


}
