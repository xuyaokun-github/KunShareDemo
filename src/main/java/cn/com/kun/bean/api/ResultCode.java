package cn.com.kun.bean.api;

/**
 * 返回码定义
 *
 * author:xuyaokun_kzx
 * date:2022/8/17
 * desc:
*/
public enum ResultCode {

    SUCCESS("000000", "操作成功"),
    FAILED("1001", "响应失败"),
    VALIDATE_FAILED("1002", "参数校验失败"),
    //返回上游的消息能尽量具体，能体现出是哪个参数导致了问题，例如
    STUDENT_NOT_FOUND("001001", "学生ID【%s】不存在，请检查"),
    STUDENT_AND_CLASS_NOT_FOUND("001002", "学生ID【%s】和课程ID【%s】均不存在，请检查"),
    ERROR("999999", "未知错误");

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
