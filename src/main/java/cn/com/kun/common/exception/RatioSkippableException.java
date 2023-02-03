package cn.com.kun.common.exception;

/**
 * 自定义的跳过策略-支持按比例判断
 *
 * author:xuyaokun_kzx
 * date:2023/2/2
 * desc:
*/
public class RatioSkippableException extends SkippableException {

    private static final long serialVersionUID = 1L;

    /**
     * 已解析（已读取）的总数
     */
    private int readTotalCount;

    public RatioSkippableException() {
        super();
    }

    public RatioSkippableException(String message) {
        super(message);
    }

    public RatioSkippableException(String message, Throwable cause) {
        super(message, cause);
    }


    public RatioSkippableException(Throwable cause) {
        super(cause);
    }

    public RatioSkippableException(String message, int readTotalCount) {
        super(message);
        this.readTotalCount = readTotalCount;
    }

    public int getReadTotalCount() {
        return readTotalCount;
    }

    public void setReadTotalCount(int readTotalCount) {
        this.readTotalCount = readTotalCount;
    }
}