package cn.com.kun.springframework.springredis.service;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import cn.com.kun.springframework.springredis.vo.JobVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 基于zset实现的任务优先级队列
 * author:xuyaokun_kzx
 * date:2021/7/27
 * desc:
*/
@Service
public class RedisJobPriorityQueueService {

    public final static Logger LOGGER = LoggerFactory.getLogger(RedisJobPriorityQueueService.class);

    private String key = "pending-job-list";//待处理任务列表

    @Autowired
    RedisTemplateHelper redisTemplateHelper;

    /**
     * 新增任务
     * 优先级高的放队头
     */
    public void push(JobVO jobVO){

        /*
            是否需要将 添加元素和弹出元素 进行互斥？
            首先分析假如不互斥可能会有什么问题？
            我在查询时，最大优先级假设是90，当线程准备进行remove弹出之前，这时候其他线程添加了个优先级为95的，
            这个时候，其实最终被弹出的还是90！
            假如你的业务允许这种情况，那就不需要做互斥，因为业务可以接受。
            假如业务接受不了，那就做互斥，保证在弹出时不能做新增操作，只有弹出完毕之后才可以做新增。
            依我看，大多数业务其实没有这个必要做互斥。
         */
        redisTemplateHelper.zsetAdd(key, JacksonUtils.toJSONString(jobVO), jobVO.getPriority());
    }

    /**
     * 弹出队列中的第一个任务
     * @return
     */
    public JobVO pop(){

        /*
            注意弹出是由两个动作构成，先查询拿到目标数据然后再删除，这个过程不是原子性的
            因此需要考虑线程安全问题
            假如不控制互斥，可能会出现：
            同一个元素，被多次查询到，然后做了重复删除。
            为了避免这个问题，需要在删除时拿到返回值，判断是否真正删除成功？
         */
        Set<String> result = redisTemplateHelper.zsetReverseRange(key,0, 0);
        for (String value : result) {
            //只需要遍历一次，因为查询返回只可能有一个元素
            /*
                删除操作是精确匹配，假如value相等，才会进行删除
             */
            Long deleteRes = redisTemplateHelper.zsetRemove(key, value);
            if (deleteRes > 0){
                //假如大于0，说明删除成功，否则说明存在并发，该元素已经被其他线程成功弹出（不需要再额外上锁就能解决线程安全问题）
                LOGGER.info("线程：{} 弹出内容：{}", Thread.currentThread().getName(), value);
                return JacksonUtils.toJavaObject(value, JobVO.class);
            }else {
                LOGGER.info("线程：{} 查询到内容：{}，但不能弹出！", Thread.currentThread().getName(), value);
                return null;
            }
        }
        return null;
    }

    /**
     * 返回任务总数
     */
    public Long count(){

        return redisTemplateHelper.zsetSize(key);
    }

}
