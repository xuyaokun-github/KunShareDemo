package cn.com.kun.foo.javacommon.designPattern.other.downstream;

/**
 * 统一的下游返回，但是并不是所有下游都返回线相同的字段名，如何做适配呢？
 *
 * author:xuyaokun_kzx
 * date:2021/12/14
 * desc:
*/
public abstract class UniRepsonse<T> {

    private String returnCode;

    private String returnMsg;

    private T data;

    /**
     * 提供一个方法做转换
     */
    abstract void tranfer();

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
