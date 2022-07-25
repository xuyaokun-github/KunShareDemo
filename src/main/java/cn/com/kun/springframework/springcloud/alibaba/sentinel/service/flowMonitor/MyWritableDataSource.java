package cn.com.kun.springframework.springcloud.alibaba.sentinel.service.flowMonitor;

import cn.com.kun.common.utils.JacksonUtils;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 流控规则更新时，会触发该方法
 *
 * author:xuyaokun_kzx
 * date:2021/9/30
 * desc:
*/
@Component
public class MyWritableDataSource implements WritableDataSource<List<FlowRule>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyWritableDataSource.class);

    @Override
    public void write(List<FlowRule> rules) throws Exception {
        LOGGER.info("MyWritableDataSource本次写入数据源，内容：{}", JacksonUtils.toJSONString(rules));
    }

    @Override
    public void close() throws Exception {

    }
}
