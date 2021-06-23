package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.vo.user.UserQueryParam;
import cn.com.kun.common.vo.user.UserQueryResVO;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserErrorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示一个PageHelper的误区
 *
 * author:xuyaokun_kzx
 * date:2021/6/1
 * desc:
*/
@Service
public class UserErrorServiceImpl implements UserErrorService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 这个方法是实现BaseService的
     * 这里错就错在，里面的dao层拿到的是User类型，但是方法的返回类型却是UserQueryResVO
     *
     * @param userQueryParam
     * @return
     */
    @Override
    public List<UserQueryResVO> list(UserQueryParam userQueryParam) {

        //这是正常的dao查询，返回的POJO是数据库层的实体类
        List<User> userList = userMapper.list(userQueryParam);
        //现在需要转换，因为返回给前端的是另一个VO
        List<UserQueryResVO> resVOList = new ArrayList<>();
        if (userList != null){
            for (User user : userList){
                UserQueryResVO userQueryResVO = new UserQueryResVO();
                BeanUtils.copyProperties(user, userQueryResVO);
                resVOList.add(userQueryResVO);
            }
        }
        return resVOList;
    }
}
