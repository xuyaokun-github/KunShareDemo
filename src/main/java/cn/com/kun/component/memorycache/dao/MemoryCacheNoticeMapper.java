package cn.com.kun.component.memorycache.dao;

import cn.com.kun.component.memorycache.entity.MemoryCacheNoticeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 改成用自定义JDBC实现
@Mapper
public interface MemoryCacheNoticeMapper {

    void save(MemoryCacheNoticeDO noticeMsgDO);

    List<MemoryCacheNoticeDO> query(@Param("clusterName") String clusterName);

    void delete(Long id);

}
