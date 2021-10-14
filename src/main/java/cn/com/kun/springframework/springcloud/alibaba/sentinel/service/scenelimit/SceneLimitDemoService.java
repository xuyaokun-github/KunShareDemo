package cn.com.kun.springframework.springcloud.alibaba.sentinel.service.scenelimit;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.FlowMonitorCallback;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.SentinelFlowMonitor;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.FlowMonitorRes;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.*;

/**
 * 按场景限流demo
 * 多规则限流--分场景限流
 *
 * <p>
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
 */
@Service
public class SceneLimitDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SceneLimitDemoService.class);

    private AtomicLong sleepMillis = new AtomicLong(100L);

    @Autowired
    SentinelFlowMonitor sentinelFlowMonitor;

    private FlowMonitorRes flowMonitorRes;

    public FlowMonitorRes getFlowMonitorRes() {
        return flowMonitorRes;
    }

    public void setFlowMonitorRes(FlowMonitorRes flowMonitorRes) {
        this.flowMonitorRes = flowMonitorRes;
    }

    @PostConstruct
    public void init(){

        /*
            注册一个回调
            注册动作由使用方自行决定
         */
        sentinelFlowMonitor.registFlowMonitorCallback(CONTEXT_MSG_PUSH, new FlowMonitorCallback() {
            @Override
            public void monitorCallback(FlowMonitorRes flowMonitorRes) {
//                LOGGER.info("触发了业务层注册的回调逻辑：{}", flowMonitorRes);
                setFlowMonitorRes(flowMonitorRes);
            }
        });

//        for (int i = 0; i < 10; i++) {
//            new Thread(()->{
//                while (true){
//                    try {
//                        //
//                        method(null, "DX");
//                        method(null, "WX");
//                        Thread.sleep(sleepMillis.get());
//                    } catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
    }

    /**
     * 根据不同的入参决定采用哪个限流配置
     * 限流后不会重试
     * @param paramMap
     * @param sendChannel
     * @return
     */
    public ResultVo method(Map<String, String> paramMap, String sendChannel) {

        ContextUtil.enter(CONTEXT_MSG_PUSH);

        //根据业务标识，决定采用哪个限流配置
        String resourceName = "";
        if ("DX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_DX;
        } else if ("WX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_WX;
        }

        try (Entry entry = SphU.entry(resourceName)) {
            // 被保护的逻辑
            return doWork(paramMap, sendChannel);
        } catch (BlockException ex) {
            //处理被流控的逻辑
            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//            LOGGER.error("触发限流--" + sendChannel, ex);
            //假如出现了限流，由当前线程一直处理，或者是丢弃、或者是放到其他队列，由异步线程去逐个处理
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     * 限流后会进行重试
     * @param paramMap
     * @param sendChannel
     * @return
     */
    public ResultVo method2(Map<String, String> paramMap, String sendChannel) {

        //ContextUtil.enter标记调用链路
        ContextUtil.enter(CONTEXT_MSG_PUSH);

        ResultVo res = null;
        //根据业务标识，决定采用哪个限流配置
        String resourceName = "";
        if ("DX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_DX;
        } else if ("WX".equals(sendChannel)) {
            resourceName = RESOURCE_SCENE_WX;
        }

        while (true){
            try (Entry entry = SphU.entry(resourceName)) {
                // 被保护的逻辑
                res = doWork(paramMap, sendChannel);
                break;
            } catch (Exception ex) {
                if (ex instanceof BlockException){
                    //BlockException
                    //处理被流控的逻辑
                    //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//                    LOGGER.error("触发限流--" + sendChannel, ex);
                    //假如出现了限流，由当前线程一直处理，或者是丢弃、或者是放到其他队列，由异步线程去逐个处理
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    res = ResultVo.valueOfError("出现非限流异常");
                    break;
                }
            }
        }
        return res;
    }


    private ResultVo doWork(Map<String, String> paramMap, String sendChannel){
//        LOGGER.info("SceneLimitDemoService在调用第三方接口，场景：{}", sendChannel);
        try {
            //加个超时时间，验证 匀速通过场景的超时时间
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        LOGGER.info("SceneLimitDemoService结束调用第三方接口，场景：{}", sendChannel);
        return ResultVo.valueOfSuccess();
    }

}
