package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.User;
import cn.com.kun.bean.model.user.UserQueryParam;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
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
     * 非分页查询
     * @param userQueryParam
     * @return
     */
    @Override
    public List<User> selectAllByMoreResultMap(UserQueryParam userQueryParam) {
        return userMapper.selectAllByMoreResultMap(0);
    }

    @Transactional
    @Override
    public int update(User user) {

        return userMapper.update(user);
    }

    @Transactional
    @Override
    public int updateMore(User user) {

        userMapper.update(user);
        userMapper.update(user);
        userMapper.update(user);
        return 0;
    }

    public void method(){

        /*
            扣钱场景
            例如我需要扣100元
            update tbl_money set 剩余的={剩余的 - 100}  where 剩余的 > 100 and userId = "张三";
            返回1，说明扣成功了，返回0，说明不足扣取，失败！
         */

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

    /**
     * 批量方式插入
     * @param userList
     * @return
     */
    @Override
    public boolean saveBatch(List<User> userList) {

        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
        UserMapper mapper = session.getMapper(UserMapper.class);
        for (int i = 0; i < userList.size(); i++) {
            mapper.insert(userList.get(i));
            if(i%1000==999){
                //每1000条提交一次防止内存溢出
                session.commit();
                session.clearCache();
            }
        }
        session.commit();
        session.clearCache();
        return false;
    }
}
