package cn.com.kun.foo.javacommon.innerclass;

import java.util.ArrayList;
import java.util.List;

public class InnerClassMemProblemDemo {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        int counter = 0;
        while (true) {
            list.add(new OuterDemoClass(100000).createInner());
            System.out.println(counter++);
        }

    }

}