package cn.com.kun.mapper;

import cn.com.kun.component.clusterlock.dblock.PessimisticLockDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * tbl_pessimistic_lock
 */
@Mapper
public interface PessimisticLockMapper {


    PessimisticLockDO acquireLock(Map<String, String> resourseName);

}
