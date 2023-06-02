package cn.com.kun.service.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.mapper.UserMapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MybatisCursorDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MybatisCursorDemoService.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

//    @Scheduled(cron = "0/10 * * * * ?") //上一次执行完才会开始下一次
//    @Scheduled(fixedRate = 3000) //上一次执行完才会开始下一次
//    @Scheduled(fixedDelay = 3000) //上一次执行完才会开始下一次
    public void scheduled(){

        LOGGER.info("MybatisCursorDemoService开始调度:" + DateUtils.now());
//        ThreadUtils.sleep(5 * 3600 * 1000);
//        ThreadUtils.sleep(2000);

    }

    /**
     * 测试流式查询
     * 真流式查询
     *
     * @return
     */
    public ResultVo testRealCursor(){
        try (
                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
                //自动提交：设置为true,表示自动提交无需事务
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Cursor<User> cursor = sqlSession.getMapper(UserMapper.class).findAllStream2()
        ) {
            cursor.forEach(user -> {
//                System.out.println(user);
                LOGGER.info("user info:{}", JacksonUtils.toJSONString(user));
                //默认一个长耗时操作，观察数据库长事务状态
                ThreadUtils.sleep(5000);
                //删除操作
//                userMapper.deleteById(user.getId());

                LOGGER.info("是否处在事务中：{}", TransactionSynchronizationManager.isSynchronizationActive());

            });
            //索引是从0开始！
            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVo.valueOfSuccess("SUCCESS");
    }




    /**
     * 测试流式查询
     * @return
     */
    public ResultVo testCursorWithDelete(){
        try (
                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Cursor<User> cursor = sqlSession.getMapper(UserMapper.class).findAllStream()
        ) {
            cursor.forEach(user -> {
//                System.out.println(user);
                LOGGER.info("user info:{}", JacksonUtils.toJSONString(user));
                //删除操作
                userMapper.deleteByFirstname(user.getFirstname());
            });
            //索引是从0开始！
            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultVo.valueOfSuccess("SUCCESS");
    }

    /**
     * 测试如何中断流式查询
     *
     * @return
     */
    public ResultVo testCursorBreak() {

        try (
                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Cursor<User> cursor = sqlSession.getMapper(UserMapper.class).findAllStream2()
        ) {
            AtomicLong atomicLong = new AtomicLong(0);
            cursor.forEach(user -> {
                LOGGER.info("user info:{}", JacksonUtils.toJSONString(user));
                ThreadUtils.sleep(1000);
                atomicLong.incrementAndGet();
                if (atomicLong.get() > 10){
                    //return无法正常打断，得用异常中断。
                    return;
                }
            });
            //索引是从0开始！
            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultVo.valueOfSuccess("SUCCESS");
    }

    /**
     *
     * @return
     */
    public ResultVo testFakeCursor() {

        //和写法没有关系，和fetchSize有关系
        try (
                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Cursor<User> cursor = sqlSession.getMapper(UserMapper.class).findAllStream()
        ) {
            cursor.forEach(user -> {
//                System.out.println(user);
                LOGGER.info("user info:{}", JacksonUtils.toJSONString(user));
            });
            //索引是从0开始！
            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResultVo.valueOfSuccess("SUCCESS");
    }

    public ResultVo testCursorTrx() {

        try (
                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
                //自动提交：设置为true,表示自动提交无需事务   默认为false
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Cursor<User> cursor = sqlSession.getMapper(UserMapper.class).findAllStream2()
        ) {
            cursor.forEach(user -> {
//                System.out.println(user);
                LOGGER.info("user info:{}", JacksonUtils.toJSONString(user));
                //默认一个长耗时操作，观察数据库长事务状态
                ThreadUtils.sleep(5000);
                //删除操作
//                userMapper.deleteById(user.getId());

            });
            //索引是从0开始！
            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultVo.valueOfSuccess("SUCCESS");
    }
}
