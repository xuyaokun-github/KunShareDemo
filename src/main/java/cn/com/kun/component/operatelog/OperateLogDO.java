package cn.com.kun.component.operatelog;

public class OperateLogDO {

    private String moduleName;

    /**
     * 操作详情
     */
    private String operDetail;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOperDetail() {
        return operDetail;
    }

    public void setOperDetail(String operDetail) {
        this.operDetail = operDetail;
    }


    @Override
    public String toString() {
        return "OperateLogDO{" +
                "moduleName='" + moduleName + '\'' +
                ", operDetail='" + operDetail + '\'' +
                '}';
    }


}
