package cn.com.kun.foo.javacommon.lambda;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestSort {

    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add(String.valueOf(i));
        }
        Set<Integer> set2 = set.stream().map(string->Integer.valueOf(string)).collect(Collectors.toSet());
        List<Integer> list = set2.stream().sorted().collect(Collectors.toList());
        for (Integer str :list){
            System.out.print(str + " ");
        }

//        List<Integer> list = set.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//        List<Integer> list2 = set.stream().sorted().collect(Collectors.toList());

//        System.out.println(list);
//        System.out.println(list2);

    }
}
