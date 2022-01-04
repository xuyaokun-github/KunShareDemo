package cn.com.kun.foo.javacommon.designPattern.other.downstream;

public class SmsResponse extends UniRepsonse{

    private String code1;

    private String message1;

    private Object data;

    @Override
    void tranfer() {
        this.setReturnCode(this.code1);
        this.setReturnMsg(this.message1);
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
