package cn.com.kun.apache.flink.flinkkafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class FlinkTopicMsg {

    /**
     * 上游系统传递给短信平台的ID
     */
    private String msgId;

    /**
     * 运营商返回的ID
     */
    private String tradeId;

    /**
     * 运行商返回的状态码 A-Z
     */
    private String statusCode;

    /**
     * 服务端返回的消息是字符串类型，需要转换成date类型，必须加@JsonFormat注解
     * 否则序列化，拿到的对象是空
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
