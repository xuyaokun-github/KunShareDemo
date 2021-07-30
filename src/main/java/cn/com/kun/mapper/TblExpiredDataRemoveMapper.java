package cn.com.kun.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TblExpiredDataRemoveMapper {

    /**
     * 清除数据
     * @param map
     * @return
     */
    int removeData(Map map);

}
