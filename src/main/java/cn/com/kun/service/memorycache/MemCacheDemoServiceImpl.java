package cn.com.kun.service.memorycache;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.component.memorycache.annotation.EvictCacheNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MemCacheDemoServiceImpl implements IMemCacheDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemCacheDemoServiceImpl.class);

    @EvictCacheNotice(configName = "memorycache-student-service", key = "#reqVO.id.toString()")
    @Override
    public Integer updateStudent(StudentReqVO reqVO) {

        LOGGER.info("我是远程其他服务，开始更新学生信息");
        return 1;
    }
}
