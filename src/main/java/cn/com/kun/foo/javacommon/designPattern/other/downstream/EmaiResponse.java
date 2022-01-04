package cn.com.kun.foo.javacommon.designPattern.other.downstream;

public class EmaiResponse extends UniRepsonse{

    private String code;

    private String message;

    private Object data;

    @Override
    void tranfer() {
        this.setReturnCode(this.code);
        this.setReturnMsg(this.message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
