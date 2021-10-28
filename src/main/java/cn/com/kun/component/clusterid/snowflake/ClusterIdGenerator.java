package cn.com.kun.component.clusterid.snowflake;

import cn.com.kun.common.utils.InetAddressUtils;
import cn.com.kun.component.clusterlock.redislock.RedisClusterLockHandler;
import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 分布式ID工具类-基于雪花算法
 * 1.扩展了workerId的生成
 * 2.解决了时钟回拨问题
 *
 * author:xuyaokun_kzx
 * date:2021/7/13
 * desc:
*/
@Component
public class ClusterIdGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterIdGenerator.class);

    private String WORKERID_KEY_PREFIX = "WORKERID";

    /**
     * 机器ID
     */
    @Value("${clusterId.datacenterId:1}")
    private long datacenterId;

    IdWorker idWorker;

    long workerId;

    private String hostname;

    @Autowired
    RedisTemplateHelper redisTemplateHelper;

    /**
     * 分布式锁
     */
    @Autowired
    RedisClusterLockHandler redisClusterLockHandler;

    @PostConstruct
    public void init(){
        hostname = InetAddressUtils.getHostName();
        workerId = getWorkerId();
        idWorker = new IdWorker(workerId, datacenterId);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "ClusterIdGenerator-keeplive-thread");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> refreshIdLiveTime(), 1, 1, TimeUnit.MINUTES);
    }

    //定时发送心跳
    public void refreshIdLiveTime(){
        try {
            //当前节点主机名
//            LOGGER.info("refreshIdLiveTime, workerId:{}",  workerId);
            //一分钟刷新一次，更新有效时间为2分钟
            // （假如2分钟内重启很多次，可能会获取不到workerId,所以可以将刷新间隔尽量设置小一点，存活时间尽量短一点）
            redisTemplateHelper.set(WORKERID_KEY_PREFIX + ":" + workerId, hostname, 120);
        }catch (Exception e){
            LOGGER.error("刷新workerId有效时间异常", e);
        }

    }



    /**
     * 获取机器ID
     * @return
     */
    private long getWorkerId() {

        //同一个集群内抢锁（同一套redis里）
        String lockKey = "workerid-lockkey";
        long number = 1;
        //上锁
        redisClusterLockHandler.lock(lockKey);
        //假如获取失败，直接抛异常，说明环境有问题
        while (true){
            number = redisTemplateHelper.incr("workerid-number", 1);
            if (number >= 32){
                number = 1;
                redisTemplateHelper.set("workerid-number", number);
            }
            String key = WORKERID_KEY_PREFIX + ":" + number;
            if (redisTemplateHelper.hasKey(key)){
                //假如存在，则不能用这个id，继续递增
                continue;
            }else {
                //假如不存在，说明可用
                redisTemplateHelper.set(key, hostname, 120);
                LOGGER.info("获取到分布式ID生成器对应的workerId:{}",  number);
                break;
            }
        }

        //解锁
        redisClusterLockHandler.unlock(lockKey);
        return number;
    }

    public long id(){
        return idWorker.nextId();
    }


    /**
     * 获取string形式
     * @return
     */
    public String idStr(){
        return String.valueOf(idWorker.nextId());
    }

}
