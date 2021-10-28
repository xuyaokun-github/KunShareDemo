package cn.com.kun.component.redo.lock;

public interface LockControl {

    boolean lock(String resourcName);

    boolean unlock(String resourcName);

}
