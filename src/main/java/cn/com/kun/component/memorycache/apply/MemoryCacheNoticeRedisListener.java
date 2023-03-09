package cn.com.kun.component.memorycache.apply;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.UnsupportedEncodingException;


/**
 * 内存缓存通知监听器
 * 收到消息就清空对应内存缓存
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
public class MemoryCacheNoticeRedisListener implements MessageListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheNoticeRedisListener.class);

    @Autowired
    private MemoryCacheRedisDetector memoryCacheRedisDetector;

    @Autowired
    private MemoryCacheCleaner memoryCacheCleaner;

    //监听方法，一收到消息，就会触发这个方法
    @Override
    public void onMessage(Message message, byte[] pattern) {

        if (message != null){
            try {
                String msg = new String(message.getBody(),"UTF-8");
                MemoryCacheNoticeMsg noticeMsg = JacksonUtils.toJavaObject(msg, MemoryCacheNoticeMsg.class);
                LOGGER.info("收到的内存缓存通知消息：{} 转换后：{}", msg, JacksonUtils.toJSONString(noticeMsg));
                //开始清内存缓存
                memoryCacheCleaner.clearCache(noticeMsg.getConfigName(), noticeMsg.getBizKey());
                LOGGER.info("清内存缓存成功 ConfigName：{} BizKey：{}", noticeMsg.getConfigName(), noticeMsg.getBizKey());
                //更新时间戳
                memoryCacheRedisDetector.updateTimemillis(noticeMsg.getConfigName(), noticeMsg.getUpdateTimemillis());
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("MemoryCacheNoticeRedisListenery异常", e);
            }
        }
    }

}
