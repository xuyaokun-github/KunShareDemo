package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.user.UserQueryParam;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceServiceImpl.class);

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

    /**
     * 覆盖父类的方法（也不可以不覆盖，假如不覆盖就表示用父类的默认的排序规则）
     * @return
     */
//    @Override
//    public String getOrderBy() {
//
//        //可以自由选择，用什么排序
//        return "create_time desc";
//    }
}
