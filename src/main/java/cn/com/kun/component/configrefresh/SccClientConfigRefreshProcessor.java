package cn.com.kun.component.configrefresh;

import cn.com.kun.component.configrefresh.refreshedhandler.SccConfigRefreshedHandler;
import cn.com.kun.component.memorycache.apply.MemoryCacheRedisDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * SCC（Spring Cloud Config）客户端配置刷新处理器
 * 作用：定期刷新spring容器里的配置属性，各个服务层能使用到新配置
 *
 * author:xuyaokun_kzx
 * date:2021/8/6
 * desc:
*/
@Component
public class SccClientConfigRefreshProcessor implements ApplicationContextAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheRedisDetector.class);


    ApplicationContext context;

    @Autowired
    private Environment environment;

    /**
     * spring的类
     */
    @Autowired
    RefreshEndpoint refreshEndpoint;

    /**
     * 当前的版本号
     */
    private String currentConfigVersion = "";

    /**
     * 组件的全局开关
     */
    @Value("${custom.sccClientConfigRefresh.enabled:true}")
    private boolean refreshedEnabled;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    //调度，30秒一次（视业务决定，假如实效性要求高，该值可以设小）
//    @Scheduled(fixedRate = 30000L)
    public void schedule(){

        //判断全局开关是否启用（假如使用外部接口刷新，则客户端主动刷新机制可以停用）
        if (!refreshedEnabled){
            return;
        }

        //如何判断是否需要刷新配置？
        boolean isNeedRefresh = checkNeedRefresh();

        if (!isNeedRefresh){
            //无需刷新，返回
            return;
        }
        LOGGER.info("开始调用refreshEndpoint.refresh()");
        /*
            由客户端自行触发 /refresh接口所处理的逻辑
            /refresh接口实际调用的正是refreshEndpoint.refresh()
         */
        //这里会去重新拉取配置中心
        Collection<String> refreshedKeyList = refreshEndpoint.refresh();

        if (refreshedKeyList.isEmpty()){
            //无刷新的key,无需处理
            return;
        }
        printRefreshedKeyList(refreshedKeyList);

        /*
            假如调用了refreshEndpoint.refresh()，就不需要再主动去拉取了
            //        PropertySource propertySource = configServicePropertySourceLocator.locate(environment);
            因为refreshEndpoint.refresh()里会有拉取的动作
         */
        LOGGER.info("开始触发SccConfigRefreshedHandler处理");
        //开始触发各个业务层，配置刷新后的处理逻辑
        /*
            这里可以设计一个接口，让各个服务层选择性去实现,方便扩展
            因为这个类是组件公共层，每个业务独有的逻辑放到实现层去
            这里有一点需要注意，只要发生了配置变更，即使是很小的一处变更，都会导致所有SccConfigRefreshedHandler实现类执行
            这里可以继续优化：有多种解决思路
            1.可以将版本号机制继续细化，对应每一个实现层，假如判断到版本号有变，再触发该实现层
            2.因为上面可以将发生过变化的key拿到，假如含有该实现层对应的key,再触发，否则不触发
            3.将版本号判断下沉到各个实现层中，由各个实现层自己判断是否需要真正触发具体逻辑
            三个思路各有优缺点，选其一即可，这就是设计的作用，具体场景具体分析，必须要避免无用的触发。
            我更推荐方案2
         */

        List<SccConfigRefreshedHandler> sccConfigRefreshedHandlers = getAllSccConfigRefreshedHandlers();
        invokeSccConfigRefreshedHandlers(sccConfigRefreshedHandlers, refreshedKeyList);
    }

    private void invokeSccConfigRefreshedHandlers(List<SccConfigRefreshedHandler> sccConfigRefreshedHandlers, Collection<String> refreshedKeyList) {

        if (!sccConfigRefreshedHandlers.isEmpty()){
            for (SccConfigRefreshedHandler handler : sccConfigRefreshedHandlers){
                handler.refreshed(refreshedKeyList);
            }
        }
    }

    private List<SccConfigRefreshedHandler> getAllSccConfigRefreshedHandlers() {

        List<SccConfigRefreshedHandler> sccConfigRefreshedHandlerList = new ArrayList<>();
        Map<String, SccConfigRefreshedHandler> beanMap = context.getBeansOfType(SccConfigRefreshedHandler.class);
        beanMap.forEach((k, v) ->{
            sccConfigRefreshedHandlerList.add(v);
        });
        return sccConfigRefreshedHandlerList;
    }

    private boolean checkNeedRefresh() {

        if (true){//调试阶段
            return true;
        }

        /*
            考虑到每个工程都去拉配置中心，然后再判断 配置版本是否有变更，这样的代码很大
            通常生产配置中心只有一套，而不同的项目组微服务可能会有多个，所以为了减少对配置中心的访问，可以使用一层redis缓存
            每次先判断redis里是否有新版本，假如有再拉取配置中心。
            每次配置文件变更之后，管理员可以通过一个接口，将一个新版本放入redis,由各个微服务自己按需去判断redis.
         */
        String remoteConfigVersion = getConfigVersionFromRedis();
        if (!currentConfigVersion.equals(remoteConfigVersion)){
            //假如版本号不等，需要刷新
            return true;
        }
        return false;
    }

    private String getConfigVersionFromRedis() {

        //拼接本工程的微服务名到redis中获取版本号，因为访问redis这个代价比较小，每个项目组都有自己的redis集群
        String applicationName = environment.getProperty("");
        return "";
    }

    private void printRefreshedKeyList(Collection<String> refreshedKeyList) {

        if (!refreshedKeyList.isEmpty()){
            StringJoiner stringJoiner = new StringJoiner(",");
            refreshedKeyList.forEach(key ->{
                stringJoiner.add(key);
            });
            LOGGER.info("本次刷新的key集合：{}", stringJoiner.toString());
        }
    }

}
