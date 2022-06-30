package cn.com.kun.springframework.batch.common;

import cn.com.kun.common.exception.MyBatchBussinessException;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;

/**
 * 自定义batch异常处理器
 *
 * Created by xuyaokun On 2020/10/15 22:59
 * @desc:
 */
public class MyBatchExceptionHandler implements ExceptionHandler {

    @Override
    public void handleException(RepeatContext context, Throwable throwable) throws Throwable {


        if (throwable instanceof MyBatchBussinessException){

            //setCompleteOnly不是设置跳过，而是直接设置为任务完成！
//            context.setCompleteOnly();

            //需要设置跳过，有两种方法：调用close或者什么都不做
//            context.close();

        }else {
            //假如不是可跳过的异常，则继续抛出
            throw throwable;
        }

    }


}
