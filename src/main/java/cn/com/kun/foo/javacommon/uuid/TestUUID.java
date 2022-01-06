package cn.com.kun.foo.javacommon.uuid;

import java.util.*;

public class TestUUID {

    public static void main(String[] args) {

        //生成1万个
        List<String> uuidList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            uuidList.add(UUID.randomUUID().toString());
        }
        Set<String> uuidSet = new HashSet<>(uuidList);
        System.out.println(uuidList.size());
        System.out.println(uuidSet.size());
    }

}
