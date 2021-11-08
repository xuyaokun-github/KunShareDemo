package cn.com.kun.service.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.user.UserQueryParam;
import cn.com.kun.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<UserQueryParam, User> {

    List<User> selectAllByMoreResultMap(UserQueryParam userQueryParam);
}
