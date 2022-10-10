package cn.com.kun.foo.javacommon.exception;

import cn.com.kun.common.exception.BizException;
import cn.com.kun.common.exception.CompensableException;

public class TestCompensableException {

    public static void main(String[] args) {

        try {
            method();
        }catch (Exception e){
            //
            if (e instanceof CompensableException){
                //识别到可补偿异常
                System.out.println("识别到可补偿异常");
            }else {
                System.out.println("识别到不可补偿异常");
            }
        }
    }

    private static void method() {

        if (true){
//            throw new CompensableException("", "");
            throw new BizException();
        }
    }
}
