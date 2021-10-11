package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo1;

import cn.com.kun.common.vo.ResultVo;

public abstract class NbaTeamHandler {

    protected NbaTeamHandler successor;//通常也喜欢起名为next

    protected String handlerName;

    public NbaTeamHandler(String handlerName) {
        this.handlerName = handlerName;
    }

    /**
     * 抽象方法，留给子类实现
     * @param requirement
     * @return
     */
    public abstract ResultVo providePlayer(String requirement);

    public void setSuccessor(NbaTeamHandler successor) {
        this.successor = successor;
    }



}
