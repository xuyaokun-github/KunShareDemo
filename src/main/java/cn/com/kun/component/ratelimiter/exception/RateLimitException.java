package cn.com.kun.component.ratelimiter.exception;

/**
 * 限流异常
 * author:xuyaokun_kzx
 * date:2021/7/1
 * desc:
*/
public class RateLimitException extends RuntimeException {

    private static final String LIMIT_REJECT_RETURN_CODE = "777777";

    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public RateLimitException() {
        super();
    }

    public RateLimitException(String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = LIMIT_REJECT_RETURN_CODE;
    }

    public RateLimitException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}