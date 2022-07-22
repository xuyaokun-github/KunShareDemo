package cn.com.kun.component.memorycache.maintain;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import cn.com.kun.component.memorycache.dao.MemoryCacheNoticeMapper;
import cn.com.kun.component.redis.RedisTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.com.kun.component.memorycache.constants.MemoryCacheConstants.NOTICE_TIMEMILLIS_HASH_KEYNAME;
import static cn.com.kun.component.memorycache.constants.MemoryCacheConstants.NOTICE_TOPIC;

/**
 * 内存缓存通知处理器
 * (供发通知的服务使用)
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Component
public class MemoryCacheNoticeProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheNoticeProcessor.class);

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    @Autowired
    private MemoryCacheNoticeMapper memoryCacheNoticeMapper;

    /**
     * 发送通知至广播队列
     * @param configName
     * @param key
     */
    public void notice(String configName, String key){

        String updateTimemillis = String.valueOf(System.currentTimeMillis());
        //封装对象，发送至redis广播
        MemoryCacheNoticeMsg noticeMsg = new MemoryCacheNoticeMsg();
        noticeMsg.setConfigName(configName);
        noticeMsg.setBizKey(key);
        noticeMsg.setUpdateTimemillis(updateTimemillis);

        if (memoryCacheProperties.isMultiRedis()){
            /*
                是否存在多套redis
                假如是无法通过广播通知所有集群，只能把记录先写入到数据库，由集群自行异步获取通知
             */
            //获取集群列表（后续可以考虑实现自动发现集群列表）
            memoryCacheProperties.getClusterList().forEach(clusterName -> {
                MemoryCacheNoticeDO noticeMsgDO = new MemoryCacheNoticeDO();
                BeanUtils.copyProperties(noticeMsg, noticeMsgDO);
                noticeMsgDO.setClusterName(clusterName);
                //存DB
                memoryCacheNoticeMapper.save(noticeMsgDO);
            });

        }else {
            //假如只有单redis集群，直接发送到redis即可
            sendNoticeToRedis(noticeMsg);
        }

    }

    public void sendNoticeToRedis(MemoryCacheNoticeMsg noticeMsg){
        //这里发送消息，由使用内存缓存的服务负责接收，收到就立刻清缓存
        String msg = JacksonUtils.toJSONString(noticeMsg);
        LOGGER.info("内存缓存刷新通知报文：{}", msg);
        //注意：假如不是用字符串序列化方式的value序列化器，就不要传字符串进去，会拼多两个双引号
//        redisTemplateHelper.sendChannelTopicMsg(NOTICE_TOPIC, msg);
        redisTemplateHelper.sendChannelTopicMsg(NOTICE_TOPIC, noticeMsg);
        /**
         * 假设redis中的时间戳数据丢失了，怎么办？
         * 没关系，只要检测线程判断到值不存在，就会重新放入
         */
        //更新redis中的时间戳
        redisTemplateHelper.hset(NOTICE_TIMEMILLIS_HASH_KEYNAME, noticeMsg.getConfigName(), noticeMsg.getUpdateTimemillis());
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
