package cn.com.kun.component.redo.common;

public class RedoTaskNotFoundException extends RuntimeException {

    public RedoTaskNotFoundException() {
        super("RedoTask Not Found!");
    }

    public RedoTaskNotFoundException(String message) {
        super(message);
    }

}
