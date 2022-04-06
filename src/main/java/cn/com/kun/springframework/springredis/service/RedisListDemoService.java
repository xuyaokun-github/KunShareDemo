package cn.com.kun.springframework.springredis.service;

import cn.com.kun.component.distributedlock.redislock.RedisDistributedLockHandler;
import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import cn.com.kun.springframework.springredis.vo.JobVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现黑名单的功能-用什么数据结构，视需求决定
 *
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
@Service
public class RedisListDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisListDemoService.class);

    private String LIST_NAME = "RedisListDemoService-list";

    private String LOCK_NAME = "RedisListDemoService-list-lock";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisDistributedLockHandler redisClusterLockHandler;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    /**
     * 用List实现线程安全的一次性弹出多个元素
     * 在入队时必须要上锁，因为多线程并发读写，不把它用锁互斥开，会导致新元素被误删的情况
     * 上锁是为了保证读写的线程安全
     *
     * @param jobVO
     */
    public void add(JobVO jobVO){
        //上锁
        redisClusterLockHandler.lock(LOCK_NAME);
        try {
            redisTemplateHelper.lSet(LIST_NAME, jobVO);
        }catch (Exception e){
            LOGGER.error("入队失败", e);
        }finally {
            //解锁
            redisClusterLockHandler.unlock(LOCK_NAME);
        }
    }

    public JobVO popOne(){
        //上锁
        redisClusterLockHandler.lock(LOCK_NAME);
        try {
            return redisTemplateHelper.lLeftPop(LIST_NAME);
        }catch (Exception e){
            LOGGER.error("popOne失败", e);
        }finally {
            //解锁
            redisClusterLockHandler.unlock(LOCK_NAME);
        }
        return null;
    }

    /**
     * 一次性弹出多个元素
     * list类型并不支持一次性返回多个值并删除这些已经返回的值
     * 因此需要上锁，将弹出和删除动作捆绑为一个
     */
    public List<JobVO> popMore(int num){

        try {
            //上锁
            redisClusterLockHandler.lock(LOCK_NAME);
            //假如list为空，num超出最大长度，执行不会报错，会返回空
            List<JobVO> jobVOList = redisTemplateHelper.lGet(LIST_NAME, 0, num);
            //trim是保留的意思，假如下标超出最大长度，也不会报错，相当于do nothing
            redisTemplateHelper.lTrim(LIST_NAME, num + 1, -1);
            return jobVOList;
//            LOGGER.info("{}执行弹出", Thread.currentThread().getName());
//            Thread.sleep(1000);
//            LOGGER.info("{}结束弹出", Thread.currentThread().getName());

//            return null;
        }catch (Exception e){
            LOGGER.error("popMore异常", e);
        }finally {
            //解锁
            redisClusterLockHandler.unlock(LOCK_NAME);
        }
        return null;
    }

}
