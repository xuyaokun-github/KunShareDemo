package cn.com.kun.component.memorycache.maintain.noticeService;

import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 假如是双活集群，有两套Eureka，如何让两套Eureka都收到广播呢？
 * 可以用DB做通知层
 * 在本框架内，Eureka通知只负责针对单个集群内广播通知
 *
 * author:xuyaokun_kzx
 * desc:
*/
@Component
public class EurekaMemoryCacheNoticeServiceImpl implements MemoryCacheNoticeService, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(EurekaMemoryCacheNoticeServiceImpl.class);

    @Override
    public void sendBroadcastNotice(MemoryCacheNoticeMsg noticeMsg) {

        //找到注册中心里所有IP地址，然后请求刷新 TODO


    }

    @Override
    public void afterPropertiesSet() throws Exception {

        MemoryCacheNoticeServiceStrategyFactory.register("Eureka", this);
    }
}
