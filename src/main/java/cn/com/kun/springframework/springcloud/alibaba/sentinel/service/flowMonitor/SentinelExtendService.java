package cn.com.kun.springframework.springcloud.alibaba.sentinel.service.flowMonitor;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.CustomFetchJsonTreeHandler;
import com.alibaba.csp.sentinel.command.CommandHandler;
import com.alibaba.csp.sentinel.command.CommandResponse;
import com.alibaba.csp.sentinel.command.vo.NodeVo;
import com.alibaba.csp.sentinel.transport.command.SimpleHttpCommandCenter;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 扩展实现，实现规则写入数据源
 *
 * author:xuyaokun_kzx
 * date:2021/9/30
 * desc:
*/
@Service
public class SentinelExtendService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelExtendService.class);

    @Autowired
    MyWritableDataSource myWritableDataSource;

    @PostConstruct
    public void init(){
        WritableDataSourceRegistry.registerFlowDataSource(myWritableDataSource);
    }

    public List<NodeVo> getAllJsonInfo(){

        List<NodeVo> results = CustomFetchJsonTreeHandler.getJsonTreeForSentinelDefaultContext();
        return results;
    }

    public List<NodeVo> getAllJsonInfoByFetchJsonTreeCommandHandler(){

        CommandHandler<?> commandHandler = SimpleHttpCommandCenter.getHandler("jsonTree");
        CommandResponse commandResponse = commandHandler.handle(null);
//        JSON.toJSONString(results);
        String json = (String) commandResponse.getResult();
        LOGGER.info("簇点链路信息：{}", json);
        List<NodeVo> results = JacksonUtils.toJavaObjectList(json, NodeVo.class);
        return results;
    }


}
