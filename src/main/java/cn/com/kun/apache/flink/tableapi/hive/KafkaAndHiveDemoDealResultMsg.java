package cn.com.kun.apache.flink.tableapi.hive;

public class KafkaAndHiveDemoDealResultMsg {

    /**
     * 上游系统传递给短信平台的ID
     */
    private String msgId;

    /**
     * 运行商返回的状态码 A-Z
     */
    private String statusCode;

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
}
