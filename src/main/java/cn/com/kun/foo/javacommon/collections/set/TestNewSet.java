package cn.com.kun.foo.javacommon.collections.set;

import java.util.HashSet;
import java.util.Set;

public class TestNewSet {

    public static void main(String[] args) {

        Set<String> set = new HashSet(){{
            add("AAA");
            add("BBB");
        }};

    }
}
