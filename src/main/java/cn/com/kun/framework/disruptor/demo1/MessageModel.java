package cn.com.kun.framework.disruptor.demo1;

/**
 * 消息体
 */
public class MessageModel {

    /**
     * 被封装的具体消息内容
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "message='" + message + '\'' +
                '}';
    }
}
