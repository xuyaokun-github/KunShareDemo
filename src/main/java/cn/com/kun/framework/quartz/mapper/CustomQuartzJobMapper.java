package cn.com.kun.framework.quartz.mapper;

import cn.com.kun.framework.quartz.common.CustomQuartzJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomQuartzJobMapper {

    /**
     * 查询任务列表（自定义数据库表）
     * @param map
     * @return
     */
    List<CustomQuartzJob> query(Map map);

}
