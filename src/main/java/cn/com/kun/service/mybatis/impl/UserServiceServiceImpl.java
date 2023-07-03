package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.User;
import cn.com.kun.bean.model.user.UserQueryParam;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.UserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class UserServiceServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceServiceImpl.class);

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

    //配合batch做验证
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED) //能解决死循环问题
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED) //能解决死循环问题
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ) //存在死循环
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE) //会引发死锁问题，因为多个线程会同时执行该方法，会造成锁互相等待

//    @Transactional(isolation = Isolation.READ_COMMITTED) //存在死循环（说明事务仍是spring-batch开启的，隔离级别在注解中定义了，但没生效）
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED) //存在死循环（说明事务仍是spring-batch开启的，隔离级别在注解中定义了，但没生效）
//    @Transactional(isolation = Isolation.REPEATABLE_READ) //存在死循环（说明事务仍是spring-batch开启的，隔离级别在注解中定义了，但没生效）

    //不用spring-batch测试，单独验证
//    @Transactional //开启了事务，用默认的隔离级别（可重复读），单线程情况下不存在问题，并发情况下存在死循环
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ) //单线程情况下不存在问题，并发情况下存在死循环
//    @Transactional(isolation = Isolation.REPEATABLE_READ) //可重复读，单线程情况下不存在问题，并发情况下存在死循环
//    @Transactional(isolation = Isolation.READ_COMMITTED) //读已提交，不存在死循环问题
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED) //读未提交，不存在死循环问题
    @Transactional(isolation = Isolation.SERIALIZABLE) //不存在死循环问题，但是有死锁问题（具体原因见我另一篇分析文章）
    @Override
    public int updateOrderCount(long id, int times) {

        LOGGER.info("更新用户的下单次数");
        User user = userMapper.selectByUserIdAndOrderCount(id);

        while (true){
            Integer newOrderCount = user.getOrderCount() + times;
            int res = userMapper.updateOrderCount(user.getId(), user.getOrderCount(), newOrderCount);
            if (res > 0){
                LOGGER.info("更新成功！");
                break;
            }else {
                LOGGER.info("更新失败，继续循环");
            }
            user = userMapper.selectByUserIdAndOrderCount(user.getId());
        }

        return 0;
    }



    @Override
    public void updateOrderCount2(long id, int times) {

        userMapper.updateOrderCount2(id, times);
    }

    /**
     * 隔离级别 源码分析 TODO
     *
     * @param id
     * @param times
     */
    public void updateOrderCount3(long id, int times) {

        LOGGER.info("是否处在事务中：{} 当前隔离级别：{} ", TransactionSynchronizationManager.isSynchronizationActive(),
                TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());

        try {
            List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
            String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        }catch (Exception e){
//                e.printStackTrace();
        }

        userMapper.updateOrderCount2(id, times);
    }

    /**
     * Timeout参数失效案例
     * 方法不会抛出异常
     *
     * @param s
     * @return
     */
    @Transactional(timeout = 10)
    @Override
    public User getUserByFirstname(String s) {

        LOGGER.info(DateUtils.now());
        User user = userMapper.getUserByFirstname("");
        /*
            虽然这里有长耗时，也超过了timeout设定的阈值，但是不会抛异常
            因为后面没有再执行SQL语句了
         */
        ThreadUtils.sleep(60000 * 2);
        LOGGER.info(DateUtils.now());
        return user;
    }

    /**
     * 正例代码
     * 会抛出 org.springframework.transaction.TransactionTimedOutException 异常
     *
     * @param s
     * @return
     */
    @Transactional(timeout = 2)
    @Override
    public User getUserByFirstname2(String s) {

        /*
            socketTimeout设置为40000，事务执行60000 会怎样？
         */
        LOGGER.info(DateUtils.now());
        ThreadUtils.sleep(60 * 1000);
        User user = userMapper.getUserByFirstname("");
        LOGGER.info(DateUtils.now());
        return user;
    }

}
