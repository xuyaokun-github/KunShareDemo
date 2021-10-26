package cn.com.kun.springframework.springretry.service;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SpringRetryDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRetryDemoService.class);

    /**
     * 默认是重试三次
     * @param s
     * @return
     */
    @Retryable(listeners = {"myRetryListenerSupport"})
    //可以指定重试次数
//    @Retryable(maxAttempts = 10)
    public ResultVo test1(String s) {

        //可以拿到执行重试上下文，往里面放入自定义属性
        RetryContext retryContext = RetrySynchronizationManager.getContext();
        retryContext.setAttribute("myName", "kunghsu");

        LOGGER.info("test");
        if (true){
            //要想注解生效，必须要将异常往外抛，不然它不会重试
            //如何在重试之后入库？
           throw new RuntimeException("出现异常");
        }
        return ResultVo.valueOfError("");
    }

    /**
     * 重试次数到了之后依然失败，就会触发@Recover注解指定的方法
     * 假如方法正常结束，没有抛出异常，则@Recover注解指定的方法不会执行
     * @param s
     * @return
     */
    @Recover
    public ResultVo recover(String s) {
        LOGGER.info("触发recover方法");
        return ResultVo.valueOfSuccess("recover");
    }

    /**
     * 假如定义了异常类型，且准确的话，会比其他方法更优先执行，具体比较逻辑可以看源码
     * org.springframework.retry.annotation.RecoverAnnotationRecoveryHandler#findClosestMatch(java.lang.Object[], java.lang.Class)
     * @param runtimeException
     * @param s
     * @return
     */
    @Recover
    public ResultVo recoverWithException(RuntimeException runtimeException, String s) {

        //在这里可以做扩展，我们可以将入参入库，后续继续补偿重试
        //也可以通过参数比较，某一类用户的，才做进一步的入库
        LOGGER.info("触发recoverWithException方法");

        RetryContext retryContext = RetrySynchronizationManager.getContext();
        String str = (String) retryContext.getAttribute("myName");
        LOGGER.info("从context中获取属性：{}", str);
        
        //入库后返回给上层是成功还是失败？
        //既然入补偿表成功，可以当成是成功了，但后续还是可能失败
        //假如返回失败，后面补偿成功，那上层也不一致，我更推荐是返回成功，因为后面补偿成功的概率更高
        return ResultVo.valueOfSuccess("recoverWithException");
    }

    @Recover
    public ResultVo recover1(String s) {
        LOGGER.info("触发recover1方法");
        return ResultVo.valueOfSuccess("recover1");
    }

    /**
     * 假如有多个recover方法，它会触发哪一个呢？
     * 根据加载顺序决定，它会有一个比较的过程
     * @param s
     * @return
     */
    @Recover
    public ResultVo recover2(String s) {
        LOGGER.info("触发recover2方法");
        return ResultVo.valueOfSuccess("recover2");
    }


    @Retryable(stateful = true)
    public ResultVo testStateful(String s) {
        LOGGER.info("test");
        if (true){
            //
            throw new RuntimeException();
        }
        return ResultVo.valueOfError("");
    }

    /**
     * 使用RetryTemplate
     */
    public void testByRetryTemplate(){

        //重试策略
        final SimpleRetryPolicy policy = new SimpleRetryPolicy(3, Collections.<Class<? extends Throwable>, Boolean>
                singletonMap(Exception.class, true));
        //退避策略
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(100);

        //创建RetryTemplate
        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        final RetryCallback<Object, Exception> retryCallback = new RetryCallback<Object, Exception>() {

            public Object doWithRetry(RetryContext context) throws Exception {

                //这里放的就是具体的业务逻辑
                LOGGER.info("do some thing");
                //设置context一些属性,给RecoveryCallback传递一些属性
                context.setAttribute("key1", "value1");
                LOGGER.info(String.valueOf(context.getRetryCount()));
                throw new Exception("exception");
                //                return null;
            }

        };

        // 如果RetryCallback执行出现指定异常, 并且超过最大重试次数依旧出现指定异常的话,就执行RecoveryCallback动作
        final RecoveryCallback<Object> recoveryCallback = new RecoveryCallback<Object>() {
            public Object recover(RetryContext context) throws Exception {
                //相当于一个降级操作
                LOGGER.info("do recory operation");
                LOGGER.info((String) context.getAttribute("key1"));
                return null;
            }
        };

        try {
            //拿到业务返回
            final Object execute = retryTemplate.execute(retryCallback, recoveryCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
