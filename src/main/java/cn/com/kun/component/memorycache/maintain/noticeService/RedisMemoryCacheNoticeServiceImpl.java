package cn.com.kun.component.memorycache.maintain.noticeService;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.memorycache.redisImpl.MemoryCacheNoticeRedisVisitor;
import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.com.kun.component.memorycache.constants.MemoryCacheConstants.NOTICE_TOPIC;

/**
 * 广播通知实现-Redis(推荐)
 *
 * author:xuyaokun_kzx
 * date:2023/1/19
 * desc:
*/
@Component
public class RedisMemoryCacheNoticeServiceImpl implements MemoryCacheNoticeService, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisMemoryCacheNoticeServiceImpl.class);

    @Autowired
    private MemoryCacheNoticeRedisVisitor memoryCacheNoticeRedisVisitor;

    @Override
    public void sendBroadcastNotice(MemoryCacheNoticeMsg noticeMsg) {

        //这里发送消息，由使用内存缓存的服务负责接收，收到就立刻清缓存
        String msg = JacksonUtils.toJSONString(noticeMsg);
        LOGGER.info("内存缓存刷新通知报文：{}", msg);
        //注意：假如不是用字符串序列化方式的value序列化器，就不要传字符串进去，会拼多两个双引号
        memoryCacheNoticeRedisVisitor.convertAndSend(NOTICE_TOPIC, noticeMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        MemoryCacheNoticeServiceStrategyFactory.register("Redis", this);
    }
}
