package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.stereotype.Component;

@Component
public class XinjiangCbaTeamHandler extends CbaTeamHandler{

//    public XinjiangCbaTeamHandler(String handlerName) {
//        super(handlerName);
//    }

    @Override
    public ResultVo providePlayer(RcmRequestContext rcmRequestContext) {

        System.out.println("我是XinjiangCbaTeamHandler");
        String bizParam = (String) rcmRequestContext.getRequestContent();
        if (bizParam.contains("新疆")){
            //执行已达到目的，已经无需再继续往下执行
            rcmRequestContext.setNeedHandle(false);//打上标识
            //其实直接return，也可以达到目的
            return ResultVo.valueOfSuccess("返回一个新疆球员");
        }
        //假如没达到业务要求，就继续往下执行
        return this.successor.providePlayer(rcmRequestContext);
    }

}
