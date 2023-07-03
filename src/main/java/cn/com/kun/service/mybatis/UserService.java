package cn.com.kun.service.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.bean.model.user.UserQueryParam;
import cn.com.kun.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<UserQueryParam, User> {

    List<User> selectAllByMoreResultMap(UserQueryParam userQueryParam);

    int update(User user);

    int updateMore(User user);

    boolean saveBatch(List<User> userList);

    int updateOrderCount(long id, int times);

    void updateOrderCount2(long id, int times);

    User getUserByFirstname(String s);

    User getUserByFirstname2(String s);

}
