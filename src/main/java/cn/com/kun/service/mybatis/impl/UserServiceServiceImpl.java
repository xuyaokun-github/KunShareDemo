package cn.com.kun.service.mybatis.impl;

import cn.com.kun.common.entity.User;
import cn.com.kun.controller.mybatis.UserQueryParam;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 非分页查询
     * @param userQueryParam
     * @return
     */
    @Override
    public List<User> list(UserQueryParam userQueryParam) {
        return userMapper.list(userQueryParam);
    }

}
