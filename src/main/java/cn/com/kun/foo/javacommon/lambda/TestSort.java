package cn.com.kun.foo.javacommon.lambda;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestSort {

    public static void main(String[] args) {

        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(3);
        set.add(4);
        set.add(2);
        List<Integer> list = set.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        //升序
        List<Integer> list2 = set.stream().sorted().collect(Collectors.toList());
        System.out.println(list);
        System.out.println(list2);

    }
}
