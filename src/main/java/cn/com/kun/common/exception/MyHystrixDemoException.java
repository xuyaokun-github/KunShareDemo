package cn.com.kun.common.exception;

import com.netflix.hystrix.exception.ExceptionNotWrappedByHystrix;

/**
 * 实现了ExceptionNotWrappedByHystrix空接口
 * 目的是为了让hystrix可以跳过该异常，不进行降级处理
 *
 * author:xuyaokun_kzx
 * date:2021/9/2
 * desc:
*/
public class MyHystrixDemoException extends RuntimeException implements ExceptionNotWrappedByHystrix {


}
