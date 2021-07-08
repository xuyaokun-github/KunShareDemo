package cn.com.kun.mapper;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.user.UserQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int insert(User user);

    int update(User user);

    List<User> query(Map map);

    List<User> list(UserQueryParam userQueryParam);

    int deleteByFirstname(String firstname);

    /**
     * 流式查询
     * @return
     */
    Cursor<User> findAllStream();

}
