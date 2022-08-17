package cn.com.kun.foo.javacommon.collections.list;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestListCountLimit {

    public static void main(String[] args) {

        /*
            验证是否能放4000千万个元素
         */
        List<String> strList = new ArrayList();
        for (int i = 0; i < 4000 * 10000; i++) {
            strList.add(UUID.randomUUID().toString());
        }
        System.out.println(strList.size());
    }

}
