package cn.com.kun.component.memorycache.maintain;

import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

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
