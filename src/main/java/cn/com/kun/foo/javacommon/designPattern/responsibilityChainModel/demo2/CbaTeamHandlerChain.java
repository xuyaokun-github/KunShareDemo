package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 责任链模式有很多变体，能实现目的即可
 *
 * author:xuyaokun_kzx
 * date:2021/10/11
 * desc:
*/
@Component
public class CbaTeamHandlerChain implements InitializingBean, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private CbaTeamHandler chain;

    /**
     */
    @Deprecated
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     */
    public static ApplicationContext getContext() {
        return applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, CbaTeamHandler> beansMap = getContext().getBeansOfType(CbaTeamHandler.class);
        CbaTeamHandler guangdongCbaTeamHandler = beansMap.get("guangdongCbaTeamHandler");
        CbaTeamHandler xinjiangCbaTeamHandler = beansMap.get("xinjiangCbaTeamHandler");
        CbaTeamHandler beijingCbaTeamHandler = beansMap.get("beijingCbaTeamHandler");
        //确定链的执行顺序
        guangdongCbaTeamHandler.setSuccessor(xinjiangCbaTeamHandler);
        xinjiangCbaTeamHandler.setSuccessor(beijingCbaTeamHandler);
        //设置链的起始
        chain = guangdongCbaTeamHandler;
    }

    public CbaTeamHandler getChain(){
        return chain;
    }

    public ResultVo providePlayer(RcmRequestContext rcmRequestContext) {
        return getChain().providePlayer(rcmRequestContext);
    }
}
