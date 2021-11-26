package cn.com.kun.component.distributedId.snowflake;

import cn.com.kun.common.utils.InetAddressUtils;
import cn.com.kun.component.distributedlock.redislock.RedisDistributedLockHandler;
import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 分布式ID工具类-基于雪花算法
 * 1.扩展了workerId的生成（缺点，强依赖了redis）
 * 2.解决了时钟回拨问题
 *
 * author:xuyaokun_kzx
 * date:2021/7/13
 * desc:
*/
@Component
public class DistributedIdGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(DistributedIdGenerator.class);

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
    RedisDistributedLockHandler redisClusterLockHandler;

    @PostConstruct
    public void init(){

        new Thread(()->{
            hostname = InetAddressUtils.getHostName();
            //这里获取ID的过程中有阻塞等待锁的过程，假如一直获取不到锁，将会一直阻塞，应用始终无法正常启动
            //这里的锁，必须要设置成可自动超时，因为这个过程不是复杂的过程，锁太久说明肯定出问题
            //或者可以尝试优化成，将这个初始化的过程异步化完成，假如初始化失败，这个类对外不提供正常功能（比较推荐这个）
            workerId = getWorkerId();
            idWorker = new IdWorker(workerId, datacenterId);
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "ClusterIdGenerator-keeplive-thread");
                thread.setDaemon(true);
                return thread;
            });
            scheduler.scheduleAtFixedRate(() -> refreshIdLiveTime(), 1, 1, TimeUnit.MINUTES);
        }).start();

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
        //这里上锁的过程是阻塞的，假如一直上锁失败，这里将会一直卡住，下面程序无法继续往下，所以应该优化 TODO 设置一个等待阈值
        //假如超过这个阈值还获取不到，就抛异常结束了，不要让程序一直干等
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

        Assert.notNull(idWorker, "分布式ID组件暂不可用");
        return idWorker.nextId();
    }


    /**
     * 获取string形式
     * @return
     */
    public String idStr(){
        Assert.notNull(idWorker, "分布式ID组件暂不可用");
        return String.valueOf(idWorker.nextId());
    }

}
