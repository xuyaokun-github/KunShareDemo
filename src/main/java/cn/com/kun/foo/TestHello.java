package cn.com.kun.foo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestHello {

    private static final String aaa = "aaa";

    public static void main(String[] args) {
        System.out.println(399999998 % 2);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        });
        for (Integer i : list) {
            //依次输出1、2、3
            System.out.println(i);
        }
    }

}
