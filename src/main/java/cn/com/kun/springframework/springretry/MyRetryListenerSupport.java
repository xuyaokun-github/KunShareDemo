package cn.com.kun.springframework.springretry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;

@Component
public class MyRetryListenerSupport extends RetryListenerSupport {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyRetryListenerSupport.class);

    /**
     * 无论是否出现异常，该方法都会触发
     * @param context
     * @param callback
     * @param throwable
     * @param <T>
     * @param <E>
     */
    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {

        super.close(context, callback, throwable);
        //那如何判断是成功还是失败呢？
        //第一种方法，throwable是否为空？假如非空，说明出现错误
        LOGGER.info("retry监听器close");

        RetryContext retryContext = RetrySynchronizationManager.getContext();
        String str = (String) retryContext.getAttribute("myName");
        LOGGER.info("从context中获取属性：{}", str);

//        try {
//            //这里拿到RetryCallback，可以做什么？
//            //RetryCallback只对外部暴露了一个方法：即doWithRetry
//            LOGGER.info("retry监听器中执行doWithRetry");
//            //一般都不会在这里再次执行业务逻辑的，因为在这里就算成功了，返回给上层的结果仍是异常
//            callback.doWithRetry(context);
//        } catch (Throwable e) {
//            //
//            LOGGER.info("retry监听器中执行doWithRetry出现异常");
//        }

        /*
            作者为什么要设计RetryCallback在方法入参这里

         */


    }

    /**
     * 假如没有异常，这个方法不会被调用
     * @param context
     * @param callback
     * @param throwable
     * @param <T>
     * @param <E>
     */
    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        super.onError(context, callback, throwable);
        LOGGER.info("retry监听器onError");
    }
}
