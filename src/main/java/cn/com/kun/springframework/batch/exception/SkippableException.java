package cn.com.kun.springframework.batch.exception;

public class SkippableException extends Exception {

    private static final long serialVersionUID = 1L;

    public SkippableException() {
        super();
    }

    public SkippableException(String message) {
        super(message);
    }

    public SkippableException(String message, Throwable cause) {
        super(message, cause);
    }


    public SkippableException(Throwable cause) {
        super(cause);
    }
}