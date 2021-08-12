package cn.com.kun.bean.model;

/**
 * 系统参数
 * 1.用于展示Restful接口规范
 * 2.TODO 设计一套系统参数框架
 *
 * author:xuyaokun_kzx
 * date:2021/8/11
 * desc:
*/
public class SysParamReqVO {

    /**
     * 自增主键
     */
    private Long id;

    private String paramId;

    private String paramValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
