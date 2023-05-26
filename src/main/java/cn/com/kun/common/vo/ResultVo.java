package cn.com.kun.common.vo;

import cn.com.kun.common.advice.BaseErrorInfoInterface;

import java.io.Serializable;
import java.util.HashMap;

public class ResultVo<T> implements Serializable {

    private String message;
    private T value;
    private boolean success;
    private String msgCode;
    private HashMap resultMap;

    private ResultVo() {
    }

    public static ResultVo valueOfSuccess(Object value) {
        ResultVo vo = new ResultVo();
        vo.value = value;
        vo.success = true;
        vo.msgCode = "000000";
        vo.message = "处理成功";
        return vo;
    }

    public static ResultVo valueOfSuccess() {
        return valueOfSuccess(null);
    }

    public static ResultVo valueOfError(String msg, Object value) {
        return valueOfError(msg, "999999", null, value);
    }

    public static ResultVo valueOfError(String msg, String msgCode, Class source, Object value) {

        ResultVo vo = new ResultVo();
        vo.value = value;
        vo.message = msg;
        vo.success = false;
        vo.msgCode = msgCode;
        return vo;
    }

    public static ResultVo valueOfError(String msg) {
        return valueOfError(msg, "999999", null, null);
    }

    public static ResultVo valueOfError(String msg, String msgCode) {
        return valueOfError(msg, msgCode, null, null);
    }

    public String getMessage() {
        return message;
    }

    public ResultVo setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResultVo setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public T getValue() {
        return value;
    }

    public ResultVo setValue(T value) {
        this.value = value;
        return this;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public ResultVo setMsgCode(String msgCode) {
        this.msgCode = msgCode;
        return this;
    }

    public HashMap getResultMap() {
        return resultMap;
    }

    public void setResultMap(HashMap resultMap) {
        this.resultMap = resultMap;
    }

    /**
     * 失败
     */
    public static ResultVo error(BaseErrorInfoInterface errorInfo) {
        ResultVo rb = new ResultVo();
        rb.setMsgCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setValue(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultVo error(String smgCode, String message) {
        ResultVo rb = new ResultVo();
        rb.setMsgCode(smgCode);
        rb.setMessage(message);
        rb.setValue(null);
        return rb;
    }

    @Override
    public String toString() {
        return "ResultVo{" +
                "message='" + message + '\'' +
                ", value=" + value +
                ", success=" + success +
                ", msgCode='" + msgCode + '\'' +
                ", resultMap=" + resultMap +
                '}';
    }
}

