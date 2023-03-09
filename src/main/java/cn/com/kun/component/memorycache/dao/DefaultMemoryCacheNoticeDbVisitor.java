package cn.com.kun.component.memorycache.dao;

import cn.com.kun.component.memorycache.entity.MemoryCacheNoticeDO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认的DB访问层
 *
 * author:xuyaokun_kzx
 * date:2023/3/2
 * desc:
*/
@Component
public class DefaultMemoryCacheNoticeDbVisitor implements MemoryCacheNoticeDbVisitor {

    //使用自封装的JDBC工具类作为基础
//    @Autowired
//    CommonDbUtilsJdbcStore commonDbUtilsJdbcStore;

    @Override
    public boolean save(MemoryCacheNoticeDO noticeMsgDO) {

        //TODO

        return false;
    }

    @Override
    public List<MemoryCacheNoticeDO> query(String clusterName) {

        //TODO
        return null;
    }

    @Override
    public boolean delete(Long id) {

        //TODO
        return false;
    }


}
