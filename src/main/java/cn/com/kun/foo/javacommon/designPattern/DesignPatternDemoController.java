package cn.com.kun.foo.javacommon.designPattern;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2.CbaTeamHandlerChain;
import cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2.RcmRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DesignPatternDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DesignPatternDemoController.class);

    @Autowired
    private CbaTeamHandlerChain cbaTeamHandlerChain;

    @GetMapping("/testResponsibilityChainModel")
    public String testResponsibilityChainModel(){

        /*
            在这里，可以通过cbaTeamHandlerChain链对象来调用，也可以通过具体的首个handler进行调用
         */

        String bizParam = "给我一个广东球员";
        RcmRequestContext rcmRequestContext = new RcmRequestContext();
        rcmRequestContext.setRequestContent(bizParam);
        //对于控制层来说，只需要调用CbaTeamHandlerChain的方法
        ResultVo resultVo = cbaTeamHandlerChain.providePlayer(rcmRequestContext);
        LOGGER.info(JacksonUtils.toJSONString(resultVo));
        bizParam = "给我一个新疆球员";
        RcmRequestContext rcmRequestContext2 = new RcmRequestContext();
        rcmRequestContext2.setRequestContent(bizParam);
        //对于控制层来说，只需要调用CbaTeamHandlerChain的方法
        ResultVo resultVo2 = cbaTeamHandlerChain.providePlayer(rcmRequestContext2);
        LOGGER.info(JacksonUtils.toJSONString(resultVo2));
        bizParam = "给我一个北京球员";
        RcmRequestContext rcmRequestContext3 = new RcmRequestContext();
        rcmRequestContext3.setRequestContent(bizParam);
        //对于控制层来说，只需要调用CbaTeamHandlerChain的方法
        ResultVo resultVo3 = cbaTeamHandlerChain.providePlayer(rcmRequestContext3);
        LOGGER.info(JacksonUtils.toJSONString(resultVo3));
        bizParam = "给我一个辽宁球员";
        RcmRequestContext rcmRequestContext4 = new RcmRequestContext();
        rcmRequestContext4.setRequestContent(bizParam);
        //对于控制层来说，只需要调用CbaTeamHandlerChain的方法
        ResultVo resultVo4 = cbaTeamHandlerChain.providePlayer(rcmRequestContext4);
        LOGGER.info(JacksonUtils.toJSONString(resultVo4));

        return "kunghsu";
    }

}
