package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.stereotype.Component;

@Component
public class BeijingCbaTeamHandler extends CbaTeamHandler{

//    public BeijingCbaTeamHandler(String handlerName) {
//        super(handlerName);
//    }

    @Override
    public ResultVo providePlayer(RcmRequestContext rcmRequestContext) {

        System.out.println("我是BeijingCbaTeamHandler");
        String bizParam = (String) rcmRequestContext.getRequestContent();
        if (bizParam.contains("北京")){
            //执行已达到目的，已经无需再继续往下执行
            rcmRequestContext.setNeedHandle(false);//打上标识
            //其实直接return，也可以达到目的
            return ResultVo.valueOfSuccess("返回一个北京球员");
        }

        //注意，这里是最后一个处理节点，不能再继续往下处理
        //假如仍不能得到正常结果，必须给个错误的返回，至此终止整个处理链
        return ResultVo.valueOfError("业务处理异常");
//        return this.successor.providePlayer(rcmRequestContext);

    }


}
