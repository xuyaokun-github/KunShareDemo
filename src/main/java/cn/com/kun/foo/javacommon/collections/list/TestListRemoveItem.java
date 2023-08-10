package cn.com.kun.foo.javacommon.collections.list;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestListRemoveItem {

    public static void main(String[] args) {

        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("a1");
        stringList.add("a1");
        stringList.add("a1");
        stringList.add("1");

        show(stringList);
        for (Iterator<String> iterator = stringList.iterator();iterator.hasNext();){
            if (iterator.next().startsWith("a")){
                iterator.remove();
            }
        }
        show(stringList);

    }

    private static void show(List<String> stringList) {
        System.out.println(JacksonUtils.toJSONString(stringList));
    }

}
