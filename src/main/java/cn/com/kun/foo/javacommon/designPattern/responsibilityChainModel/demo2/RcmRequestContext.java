package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo2;

import java.util.Map;

public class RcmRequestContext {

    private Object requestContent;

    /**
     * 存放中间数据的容器
     * 不同的handler之间可以传递数据
     */
    private Map<String, Object> contextMap;

    /**
     * 是否需要继续处理
     */
    private boolean needHandle = true;

    public Object getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(Object requestContent) {
        this.requestContent = requestContent;
    }

    public Map<String, Object> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Object> contextMap) {
        this.contextMap = contextMap;
    }

    public boolean isNeedHandle() {
        return needHandle;
    }

    public void setNeedHandle(boolean needHandle) {
        this.needHandle = needHandle;
    }
}
