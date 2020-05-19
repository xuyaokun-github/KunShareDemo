package cn.com.kun.mapper;

import cn.com.kun.common.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    void insert(User user);

    void update(User user);

    List<User> query(Map map);

    void deleteByFirstname(String firstname);
}
