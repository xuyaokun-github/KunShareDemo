package cn.com.kun.kafka.msg;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MsgCacheTopicMsg {

    private String msgId;

    private String statusCode;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTIme;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCreateTIme() {
        return createTIme;
    }

    public void setCreateTIme(Date createTIme) {
        this.createTIme = createTIme;
    }
}
