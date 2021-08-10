package cn.com.kun.component.scc.configrefresh.refreshedhandler;

import cn.com.kun.config.threadpool.CustomThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 动态调整线程池的配置
 * 基于客户端配置动态刷新实现线程池的配置动态调整
 *
 * author:xuyaokun_kzx
 * date:2021/8/6
 * desc:
*/
@Component
public class CustomThreadPoolSccConfigRefreshedHandler implements SccConfigRefreshedHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomThreadPoolSccConfigRefreshedHandler.class);

    @Autowired
    CustomThreadPoolProperties customThreadPoolProperties;

    @Override
    public void refreshed(Collection<String> refreshedKeyList) {

        //假如有自己关注的key再做更新
        String keyPrefix = "custom.threadPool";
        if (hasConcernedKey(refreshedKeyList, keyPrefix)){
            LOGGER.info("重新设定线程池配置");

        }

    }

}
