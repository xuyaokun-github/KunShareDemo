package cn.com.kun.mapper;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 */
@Mapper
public interface DeadLockDemoMapper {

    @Select("select a.ID,a.DEMO_NAME demoName " +
            "from tbl_deadlock_demo a where a.DEMO_NAME =  #{demoName} and a.DEMO_KEY = #{demoKey}")
    DeadLockDemoDO select(DeadLockDemoDO deadLockDemoDO);

    @Insert("insert into tbl_deadlock_demo (`ID`, `VERSION`, `DEMO_NAME`, `DEMO_KEY`) VALUES (#{id}, #{version}, #{demoName}, #{demoKey})")
    void insert(DeadLockDemoDO deadLockDemoDO);

    @Delete("delete from tbl_deadlock_demo")
    void deleteAll();

}
