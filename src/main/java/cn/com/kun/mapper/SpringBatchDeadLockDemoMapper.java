package cn.com.kun.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 */
@Mapper
public interface SpringBatchDeadLockDemoMapper {

    @Select("select a.JOB_INSTANCE_ID " +
            "from BATCH_JOB_INSTANCE a " +
            "order by JOB_INSTANCE_ID desc limit 1")
    int selectMaxJobInstanceId();

    @Insert("INSERT into BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, JOB_NAME, JOB_KEY, VERSION) values (#{JOB_INSTANCE_ID}, #{JOB_NAME}, #{JOB_KEY}, #{VERSION})")
    void insertBatchJobInstance(Map<String, Object> map);


}
