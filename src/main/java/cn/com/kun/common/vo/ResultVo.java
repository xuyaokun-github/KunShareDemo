package cn.com.kun.common.vo;

import java.io.Serializable;
import java.util.HashMap;

public class ResultVo
        implements Serializable {

    private static final long serialVersionUID = -4214236501903574966L;
    private String message;
    private Object value;
    private boolean success;
    private int msgCode;
    private HashMap resultMap;

    private ResultVo() {
    }

    public static ResultVo valueOfSuccess(Object value) {
        ResultVo vo = new ResultVo();
        vo.value = value;
        vo.success = true;
        return vo;
    }

    public static ResultVo valueOfSuccess() {
        return valueOfSuccess(null);
    }

    public static ResultVo valueOfError(String msg, Object value) {
        return valueOfError(msg, 0, null, value);
    }

    /**
     * ����һ���������ɴ�����Ϣ��ResultVo
     * @param msg ������Ϣ����
     * @param msgCode ������Ϣ����
     * @param source ������Դ�������������������������������Log4j��¼һ��warn��Ϣ
     * @param value ����Ҫ���ص���ʵ��
     * @return
     */
    public static ResultVo valueOfError(String msg, int msgCode, Class source, Object value) {

        ResultVo vo = new ResultVo();
        vo.value = value;
        vo.message = msg;
        vo.success = false;
        vo.msgCode = msgCode;
        return vo;
    }

    public static ResultVo valueOfError(String msg) {
        return valueOfError(msg, 0, null, null);
    }

    public static ResultVo valueOfError(String msg, int msgCode) {
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

    public Object getValue() {
        return value;
    }

    public ResultVo setValue(Object value) {
        this.value = value;
        return this;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public ResultVo setMsgCode(int msgCode) {
        this.msgCode = msgCode;
        return this;
    }

    public HashMap getResultMap() {
        return resultMap;
    }

    public void setResultMap(HashMap resultMap) {
        this.resultMap = resultMap;
    }
}

