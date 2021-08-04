package cn.com.kun.foo.javacommon.collections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestUnmodifiableSet {

    public static void main(String[] args) {

        Set<String> stringSet = new HashSet<>();
        stringSet.add("0");
        stringSet.add("1");
        stringSet.add("2");
        Set<String> newSet = Collections.unmodifiableSet(stringSet);
        //遍历正常
        for (String str : newSet) {
            System.out.println(str);
        }
        //Exception in thread "main" java.lang.UnsupportedOperationException
        try {
            newSet.add("3" + System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }

        //假如非要添加，可以针对原set进行添加(newSet能立刻感知到源set的变化，因为它是装饰模式)
        stringSet.add("4");
        for (String str : newSet) {
            System.out.println(str);
        }
    }
}
