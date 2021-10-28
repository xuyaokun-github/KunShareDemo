package cn.com.kun.component.redo.common;

public class RedoTaskCallbackNotFoundException extends RuntimeException {

//    private String errorMsg = "RedoTaskCallback Not Found!";

    public RedoTaskCallbackNotFoundException() {
        super("RedoTaskCallback Not Found!");
    }

    public RedoTaskCallbackNotFoundException(String message) {
        super(message);
    }

}
