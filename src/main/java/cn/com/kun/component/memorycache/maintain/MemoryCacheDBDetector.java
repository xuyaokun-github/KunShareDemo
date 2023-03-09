package cn.com.kun.component.memorycache.maintain;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.distributedlock.dblock.DBLock;
import cn.com.kun.component.memorycache.dao.MemoryCacheNoticeDbVisitor;
import cn.com.kun.component.memorycache.entity.MemoryCacheNoticeDO;
import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 检测数据库是否有最新的数据
 * 假如有则做通知(多活Redis架构才有必要使用)
 *
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@Component
public class MemoryCacheDBDetector {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheDBDetector.class);

    @Autowired
    private MemoryCacheNoticeProcessor memoryCacheNoticeProcessor;

    @Autowired(required = false)
    private MemoryCacheNoticeDbVisitor memoryCacheNoticeDbVisitor;

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    /**
     * 数据库锁资源名称
     */
    private final static String DBLOCK_RESOURCE_NAME = "MemoryCacheDBDetector_LOCK";

    /**
     * 检查数据库是否有待通知的记录
     * 使用数据库锁做互斥
     */
    @DBLock(resourceName = DBLOCK_RESOURCE_NAME)
    public void checkDb(){

        Assert.notNull(memoryCacheNoticeDbVisitor, "MemoryCacheNoticeDbVisitor实现不能为空");
        if (LOGGER.isDebugEnabled()){
            LOGGER.info("MemoryCacheDBDetector开始检测数据库是否有待通知数据");
        }

        //用注解上锁，保证只有一个节点进行判断后通知，可以是数据库锁（也可以是redis分布式锁，同一个集群内的分布式锁）
        String clusterName = memoryCacheProperties.getMaintain().getClusterName();
        List<MemoryCacheNoticeDO> memoryCacheNoticeDOList = memoryCacheNoticeDbVisitor.query(clusterName);
        memoryCacheNoticeDOList.forEach(memoryCacheNoticeDO -> {
            MemoryCacheNoticeMsg noticeMsg = new MemoryCacheNoticeMsg();
            BeanUtils.copyProperties(memoryCacheNoticeDO, noticeMsg);
            LOGGER.info("MemoryCacheDBDetector发送通知报文：{}", JacksonUtils.toJSONString(noticeMsg));
            memoryCacheNoticeProcessor.sendNotice(noticeMsg);
            //删记录
            memoryCacheNoticeDbVisitor.delete(memoryCacheNoticeDO.getId());
        });
    }

}
