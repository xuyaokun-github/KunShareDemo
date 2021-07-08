package cn.com.kun.mapper;

import cn.com.kun.component.memorycache.maintain.MemoryCacheNoticeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemoryCacheNoticeMapper {


    void save(MemoryCacheNoticeDO noticeMsgDO);

    List<MemoryCacheNoticeDO> query(@Param("clusterName") String clusterName);

    void delete(Long id);

}
