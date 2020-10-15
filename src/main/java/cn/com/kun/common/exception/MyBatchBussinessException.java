package cn.com.kun.common.exception;

public class MyBatchBussinessException extends Exception {

    static final long serialVersionUID = 7818375828146090155L;

    public MyBatchBussinessException() {
        super();
    }

    public MyBatchBussinessException(String message) {
        super(message);
    }

    public MyBatchBussinessException(String message, Throwable cause) {
        super(message, cause);
    }


    public MyBatchBussinessException(Throwable cause) {
        super(cause);
    }
}