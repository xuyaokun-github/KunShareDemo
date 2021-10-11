package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2;

import cn.com.kun.common.vo.ResultVo;

/**
 * 责任链模式demo--基类
 * author:xuyaokun_kzx
 * date:2021/10/11
 * desc:
*/
public abstract class CbaTeamHandler {

    protected CbaTeamHandler successor;//通常也喜欢起名为next

    protected String handlerName;

//    public CbaTeamHandler(String handlerName) {
//        this.handlerName = handlerName;
//    }

    /**
     * 抽象方法，留给子类实现
     * @param rcmRequestContext
     * @return
     */
    public abstract ResultVo providePlayer(RcmRequestContext rcmRequestContext);

    public void setSuccessor(CbaTeamHandler successor) {
        this.successor = successor;
    }

    /**
     *  优化技巧：可以用模板方法模式，将调用后继节点的逻辑提取到基类
     *  但是我这个例子里没用，需要开发人员自行确保在类里做调用，
     *  因为这个会经过测试，所以开发人员漏了调后继节点这个情况基本不会出现，这种属于比较低级的错误
     */
    public final ResultVo handle(RcmRequestContext rcmRequestContext) {
        ResultVo handled = providePlayer(rcmRequestContext);
        if (successor != null && rcmRequestContext.isNeedHandle()) {
            return successor.handle(rcmRequestContext);
        }else {
            return handled;
        }
    }

}
