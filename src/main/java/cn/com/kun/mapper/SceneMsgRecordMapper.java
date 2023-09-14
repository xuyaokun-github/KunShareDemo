package cn.com.kun.mapper;

import cn.com.kun.bean.entity.SceneMsgRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SceneMsgRecordMapper {

    int insert(SceneMsgRecordDO sceneMsgRecordDO);

}
