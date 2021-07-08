package cn.com.kun.component.memorycache.apply;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.memorycache.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.UnsupportedEncodingException;


/**
 * 内存缓存通知监听器
 * 收到消息就清空对应内存缓存
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
public class MemoryCacheNoticeListener implements MessageListener {

    public final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheNoticeListener.class);

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;

    @Autowired
    private MemoryCacheRedisDetector memoryCacheRedisDetector;

    //监听方法，一收到消息，就会触发这个方法
    @Override
    public void onMessage(Message message, byte[] pattern) {

        if (message != null){
            LOGGER.info("message.toString()：{}", message.toString());
            try {
                String msg = new String(message.getBody(),"UTF-8");
                LOGGER.info("msg：{}", msg);
                MemoryCacheNoticeMsg noticeMsg = JacksonUtils.toJavaObject(msg, MemoryCacheNoticeMsg.class);
                LOGGER.info("收到的消息：{}", JacksonUtils.toJSONString(noticeMsg));
                //开始清内存缓存
                cacheManager.getCache(noticeMsg.getConfigName()).evict(noticeMsg.getBizKey());
                //更新时间戳
                memoryCacheRedisDetector.updateTimemillis(noticeMsg.getConfigName(), noticeMsg.getUpdateTimemillis());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
