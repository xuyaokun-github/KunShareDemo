package cn.com.kun.classlibrary.rxjava.demo1;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建命名的线程池executor
 *
 */
public class NamedThreadPool {

    /**
     * 声明一个固定大小的命名ThreadPoolExecutor
     *
     * @param coreThread
     * @param maxThread
     * @param namePrefix
     * @return
     */
    public ThreadPoolExecutor newNamedThreadPoolExecutor(int coreThread, int maxThread, String namePrefix) {

        //创建自定义线程池
        return new ThreadPoolExecutor(coreThread, maxThread, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(20), new NamedThreadFactory(namePrefix),
                new ThreadPoolExecutor.AbortPolicy());
    }

    class NamedThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(0);

        private final String namePrefix;

        private ThreadFactory defaultFactory = Executors.defaultThreadFactory();

        NamedThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        /**
         * 复写newThread方法
         * 创建线程的方法
         * @param r
         * @return
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread t = defaultFactory.newThread(r);
            t.setName(namePrefix + "-" +threadNumber.incrementAndGet());
            return t;
        }
    }
}
