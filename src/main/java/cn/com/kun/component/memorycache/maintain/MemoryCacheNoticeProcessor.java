package cn.com.kun.component.memorycache.maintain;

import cn.com.kun.component.memorycache.dao.MemoryCacheNoticeDbVisitor;
import cn.com.kun.component.memorycache.entity.MemoryCacheNoticeDO;
import cn.com.kun.component.memorycache.maintain.noticeService.MemoryCacheNoticeServiceStrategyFactory;
import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import cn.com.kun.component.memorycache.redisImpl.MemoryCacheNoticeRedisVisitor;
import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.com.kun.component.memorycache.constants.MemoryCacheConstants.NOTICE_TIMEMILLIS_HASH_KEYNAME;

/**
 * 内存缓存通知处理器
 * (供发通知的服务使用)
 * 维护方的职责：
 * 1.修改具体业务值
 * 2.发出刷新通知
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Component
public class MemoryCacheNoticeProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheNoticeProcessor.class);

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    @Autowired
    private MemoryCacheNoticeDbVisitor memoryCacheNoticeDbVisitor;

    @Autowired
    private MemoryCacheNoticeRedisVisitor memoryCacheNoticeRedisVisitor;

    /**
     * 由组件使用方提供RedisTemplate实现
     * 后续可以优化成不强依赖RedisTemplate
     */
//    @Autowired(required = false)
//    private RedisTemplate redisTemplate;

    public void notice(String configName) {
        notice(configName, "");
    }

        /**
         * 发送通知至广播队列
         *
         * @param configName
         * @param key
         */
    public void notice(String configName, String key){

        //组件未启用，直接返回 do nothing
        if (!memoryCacheProperties.isEnabled()){
            return;
        }

        //封装对象，发送至redis广播
        MemoryCacheNoticeMsg noticeMsg = new MemoryCacheNoticeMsg();
        noticeMsg.setConfigName(configName);
        noticeMsg.setBizKey(key);
        noticeMsg.setUpdateTimemillis(String.valueOf(System.currentTimeMillis()));

        try {
            if (memoryCacheProperties.getMaintain().isMultiRedis()){
            /*
                是否存在多套redis
                假如是无法通过广播通知所有集群，先将记录写入到数据库，由集群自行异步获取通知然后在本集群内广播
             */
                //获取集群列表（后续可以考虑实现自动发现集群列表）
                memoryCacheProperties.getMaintain().getClusterList().forEach(clusterName -> {
                    MemoryCacheNoticeDO noticeMsgDO = new MemoryCacheNoticeDO();
                    BeanUtils.copyProperties(noticeMsg, noticeMsgDO);
                    noticeMsgDO.setClusterName(clusterName);
                    //存DB
                    memoryCacheNoticeDbVisitor.save(noticeMsgDO);
                });

            }else {
                //假如只有单redis集群，直接发送通知即可
                //通知的实现可以是Redis或者eureka
                sendNotice(noticeMsg);
            }

        }catch (Exception e){
            //是否抛出异常，由上层继续补偿处理？
            //吞掉这个异常有风险，我建议是抛出异常，由上层决定是否重试
            LOGGER.error("内存缓存刷新通知异常", e);
        }

    }

    protected void sendNotice(MemoryCacheNoticeMsg noticeMsg){

        //发送广播通知
        MemoryCacheNoticeServiceStrategyFactory.getByNoticeType(memoryCacheProperties.getNoticeImplType())
                .sendBroadcastNotice(noticeMsg);

        /**
         * 假设redis中的时间戳数据丢失了，怎么办？
         * 没关系，只要检测线程判断到值不存在，就会重新放入
         */
        //更新redis中的时间戳
        memoryCacheNoticeRedisVisitor.put(NOTICE_TIMEMILLIS_HASH_KEYNAME, noticeMsg.getConfigName(), noticeMsg.getUpdateTimemillis());

        /**
         * 假如这里精确到key级别的时间戳，是否合适？
         * 那在使用了内存缓存的服务端就需要针对key级别来判断时间，简单点就是遍历整个map，然后判断哪个时间戳过期了，然后就驱逐这个key
         * 假如key的数量较多，遍历的效率会比较低，但这种内存缓存，本来就不适用于key太多的场景
         * 一来key太多，命中率不好，内存使用率不高。二来，key太多，空间会爆。
         *
         * 但假如更新操作不是特别频繁的场景，其实只精确到configName级别已经是够用了
         *
         */
    }

}
