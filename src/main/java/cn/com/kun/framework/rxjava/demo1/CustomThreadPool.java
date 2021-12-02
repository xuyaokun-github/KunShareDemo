package cn.com.kun.framework.rxjava.demo1;

import java.util.concurrent.ThreadPoolExecutor;

public class CustomThreadPool {

    private ThreadPoolExecutor threadPoolExecutor =
            new NamedThreadPool().newNamedThreadPoolExecutor(8, 16, "CustomThreadPool");

    public ThreadPoolExecutor getExecutor() {
        return threadPoolExecutor;
    }
}
