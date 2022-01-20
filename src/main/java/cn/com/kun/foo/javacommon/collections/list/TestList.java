package cn.com.kun.foo.javacommon.collections.list;

import java.util.ArrayList;
import java.util.List;

public class TestList {

    public static void main(String[] args) {


        List list = new ArrayList();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        //把999替换到第三个位置
        list.set(0, 999);


        list.forEach(i ->{
            System.out.println(i);
        });


    }

}
