package cn.com.kun.foo.javacommon.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TestForeach {

    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }

        AtomicLong atomicLong = new AtomicLong(0);

        //反例
        list.forEach(l -> {
//            System.out.println(l);
            atomicLong.incrementAndGet();
            if (atomicLong.get() > 10){
                //这里的return指的不是指返回的return，它的作用等价于continue而已
                return;
            }
        });

        AtomicLong atomicLong2 = new AtomicLong(0);
        //正例
        try {
            list.forEach(l -> {
                System.out.println(l);
                atomicLong2.incrementAndGet();
                if (atomicLong2.get() > 10){
                    //这里的return指的不是指返回的return，它的作用等价于continue而已
                    throw new RuntimeException("主动抛出异常");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
