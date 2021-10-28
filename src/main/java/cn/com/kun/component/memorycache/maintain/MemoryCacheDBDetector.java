package cn.com.kun.component.memorycache.maintain;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.clusterlock.dblock.DBClusterLock;
import cn.com.kun.component.memorycache.MemoryCacheNoticeMsg;
import cn.com.kun.component.memorycache.MemoryCacheProperties;
import cn.com.kun.component.memorycache.dao.MemoryCacheNoticeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检测数据库是否有最新的数据
 * 假如有则做通知
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@Component
public class MemoryCacheDBDetector {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheDBDetector.class);

    @Autowired
    private MemoryCacheNoticeProcessor memoryCacheNoticeProcessor;

    @Autowired
    private MemoryCacheNoticeMapper memoryCacheNoticeMapper;

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    /**
     * 检查数据库是否有待通知的记录
     * TODO 上注解，数据库锁
     */
    @DBClusterLock(resourceName = "cn.com.kun.component.memorycache.maintain.MemoryCacheDBDetector.checkDb")
    public void checkDb(){
        //用注解上锁，保证只有一个节点进行判断后通知，可以是数据库锁（也可以是redis分布式锁，同一个集群内的分布式锁）
        String clusterName = memoryCacheProperties.getClusterName();
        List<MemoryCacheNoticeDO> memoryCacheNoticeDOList = memoryCacheNoticeMapper.query(clusterName);
        memoryCacheNoticeDOList.forEach(memoryCacheNoticeDO -> {
            MemoryCacheNoticeMsg noticeMsg = new MemoryCacheNoticeMsg();
            BeanUtils.copyProperties(memoryCacheNoticeDO, noticeMsg);
            LOGGER.info("MemoryCacheDBDetector发送通知报文：{}", JacksonUtils.toJSONString(noticeMsg));
            memoryCacheNoticeProcessor.sendNoticeToRedis(noticeMsg);
            //删记录
            memoryCacheNoticeMapper.delete(memoryCacheNoticeDO.getId());
        });
    }

}
