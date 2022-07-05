package cn.com.kun.component.operatelog;


public enum OperateTypeEnum {

    /**
     */
    SELECT("SELECT"),
    UPDATE("UPDATE"),
    INSERT("INSERT"),
    DELETE("DELETE");

    OperateTypeEnum(String operateTypeName) {
        this.operateTypeName = operateTypeName;
    }

    public String operateTypeName;

    public String getOperateTypeName() {
        return operateTypeName;
    }

    public void setOperateTypeName(String operateTypeName) {
        this.operateTypeName = operateTypeName;
    }
}
