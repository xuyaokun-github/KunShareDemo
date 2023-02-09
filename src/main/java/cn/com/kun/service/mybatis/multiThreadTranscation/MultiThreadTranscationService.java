package cn.com.kun.service.mybatis.multiThreadTranscation;

import cn.com.kun.bean.entity.User;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.MybatisCursorDemoService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程事务demo
 *
 * author:xuyaokun_kzx
 * date:2023/2/9
 * desc:
*/
@Service
public class MultiThreadTranscationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MybatisCursorDemoService.class);


    @Resource
    SqlContext sqlContext;

    @Autowired
    private UserMapper userMapper;

    /**
     * 反例
     * 测试多线程事务.
     *
     * 打开事务注解之后，
     * 假如主线程删除没用主键，就会锁表，导致其他SQL抢不到锁，执行失败。因为没命中索引，所以走的是表锁
     * 假如用主键匹配进行删除，就会锁行，其他子线程SQL能正常进行
     *
     * @param employeeDOList
     */
    @Transactional
    public void saveThread(List<User> employeeDOList) {
        try {
            //先做删除操作,如果子线程出现异常,此操作不会回滚(因为子线程和主线程的不在同一个事务中)
            userMapper.deleteById(15L);

            //获取线程池
            ExecutorService service = MybatisExecutorConfig.getThreadPool();
            //拆分数据,拆分5份
            List<List<User>> lists = averageAssign(employeeDOList, 5);
            //执行的线程
            Thread[] threadArray = new Thread[lists.size()];
            //监控子线程执行完毕,再执行主线程,要不然会导致主线程关闭,子线程也会随着关闭
            CountDownLatch countDownLatch = new CountDownLatch(lists.size());
            for (int i =0;i<lists.size();i++){

                List<User> list  = lists.get(i);
                threadArray[i] =  new Thread(() -> {
                    try {
                        //最后一个线程抛出异常
                        if (ThreadLocalRandom.current().nextInt(10) % 2 == 0){
                            throw new RuntimeException("出现异常");
                        }
                        /*
                            因为父子线程不在同一个事务中，所以子线程有些插入已经生效了，主线程的删除操作没有回滚。
                         */
                        saveBatch(list);
                    }finally {
                        countDownLatch.countDown();
                    }

                });
            }
            for (int i = 0; i <lists.size(); i++){
                service.execute(threadArray[i]);
            }
            //当子线程执行完毕时,主线程再往下执行
            countDownLatch.await();
            System.out.println("添加完毕");
        }catch (Exception e){
            LOGGER.info("error", e);
            throw new RuntimeException("出现异常");
        }
    }

    private int saveBatch(List<User> list) {

        LOGGER.info("执行保存，目标条数：{}", list.size());
//        list.forEach(item->{
//            int res = userMapper.insert(item);
//            LOGGER.info("保存结果:{}", res);
//        });
        int res = userMapper.insertByBatch(list);
        LOGGER.info("保存结果:{}", res);
        return res;
    }



    /**
     * 成功的例子
     * 测试多线程事务.
     * @param employeeDOList
     */
    public void saveThread2(List<User> employeeDOList) throws SQLException {
        // 获取数据库连接,获取会话(内部自有事务)
        SqlSession sqlSession = sqlContext.getSqlSession();
        Connection connection = sqlSession.getConnection();
        try {
            // 设置手动提交
            connection.setAutoCommit(false);
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //先做删除操作
//            employeeMapper.deleteByFirstname("1111");  //不命中索引，会锁表
            userMapper.deleteById(15L);

            ExecutorService service = MybatisExecutorConfig.getThreadPool();
            List<Callable<Integer>> callableList  = new ArrayList<>();
            List<List<User>> lists=averageAssign(employeeDOList, 5);
            for (int i =0;i<lists.size();i++){
                List<User> list  = lists.get(i);
                Callable<Integer> callable = new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        LOGGER.info("执行保存，目标条数：{}", list.size());
                        //模拟异常
                        if (ThreadLocalRandom.current().nextInt(10) % 2 == 0){
                            throw new RuntimeException("子线程处理出现异常");
                        }
                        int res = userMapper.insertByBatch(list);
                        LOGGER.info("保存结果:{}", res);
                        return res;
                    }
                };
                callableList.add(callable);
            }
            //执行子线程
            List<Future<Integer>> futures = service.invokeAll(callableList);
            for (Future<Integer> future : futures) {
                if (future.get() <= 0){
                    //假如存在任一子线程的结果失败 主线程回滚
                    //假如子线程的结果不符合预期，主线程的事务回滚
                    connection.rollback();
                    return;
                }
            }
            connection.commit();
            System.out.println("添加完毕");
        }catch (Exception e){
            connection.rollback();
            LOGGER.info("error",e);
            throw new RuntimeException("出现异常");
        }
    }


    /**
     * 平均拆分list方法.
     * @param source
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n){
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider=source.size()%n;
        int number=source.size()/n;
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value=null;
            if(remaider>0){
                value=source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }



}
