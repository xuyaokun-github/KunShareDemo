package cn.com.kun.mapper;

import cn.com.kun.bean.entity.User;
import cn.com.kun.bean.model.user.UserQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int insert(User user);

    int insertByBatch(List<User> list);

    int update(User user);

    List<User> query(Map map);

    List<User> selectAllByMoreResultMap(int id);

    List<User> list(UserQueryParam userQueryParam);

    int deleteByFirstname(String firstname);

    int deleteById(Long id);

    User getUserByFirstname(@Param("firstname") String firstname);

    /**
     * 流式查询
     * @return
     */
    Cursor<User> findAllStream();

    Long findMaxId();
}
