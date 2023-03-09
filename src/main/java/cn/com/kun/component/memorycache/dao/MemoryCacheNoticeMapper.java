package cn.com.kun.component.memorycache.dao;

import cn.com.kun.component.memorycache.entity.MemoryCacheNoticeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 改成用自定义JDBC实现
// 假如不用自定义JDBC,想用mybatis，则放开@Mapper注释
//@Mapper
public interface MemoryCacheNoticeMapper {

    void save(MemoryCacheNoticeDO noticeMsgDO);

    List<MemoryCacheNoticeDO> query(@Param("clusterName") String clusterName);

    void delete(Long id);

}
