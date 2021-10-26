package cn.com.kun.springframework.tools;

import cn.com.kun.common.exception.MyBatchBussinessException;
import org.springframework.classify.BinaryExceptionClassifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryExceptionClassifierTest {

    public static void main(String[] args) {

        BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(false);
        Map<Class<? extends Throwable>, Boolean> map = new ConcurrentHashMap<>();
        map.put(RuntimeException.class, true);
        retryableClassifier.setTypeMap(map);

        System.out.println(retryableClassifier.classify(new RuntimeException()));//true
        System.out.println(retryableClassifier.classify(new MyBatchBussinessException()));//false

        MyBatchBussinessException myBatchBussinessException = new MyBatchBussinessException();
        myBatchBussinessException.initCause(new RuntimeException());
        //虽然cause是RuntimeException，但是最外层的异常不是RuntimeException，所以结果为false
        System.out.println(retryableClassifier.classify(myBatchBussinessException));//false
    }
}
