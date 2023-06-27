package cn.com.kun.foo.javacommon.collections.map;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.*;

/**
 * 验证Map顺序问题
 * HashMap会存在乱序问题
 *
 * author:xuyaokun_kzx
 * date:2023/6/26
 * desc:
*/
public class TestMapListOrder {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("d", "d");
        map.put("b", "b");
        map.put("c", "c");
        map.put("a1", "a1");

        Collection<String> keys = map.values();
        for (String str : keys){
            //输出的顺序，和放入顺序，肯定是不同的
            //因为java.util.HashMap.Values 返回的是一个 Collection实例，它不是set也不是list，是自定义的一个实现
            System.out.println(str);
        }

        //
        System.out.println("------------------------");
        String source = "a,b,c,d,a1,b1";
        String[] arr = source.split(",");
        Map<String, String> map2 = new HashMap<>();
        /*
           输出顺序：
            a
            b
            c
            d
            a1
            b1
         */
        for(String str : arr){
            System.out.println(str);
            map2.put(str, str);
        }

        System.out.println("序列化得到Map对应的字符串为：");
        //输出的顺序：{"a1":"a1","a":"a","b":"b","c":"c","d":"d","b1":"b1"}
        String jsonString = JacksonUtils.toJSONString(map2);
        System.out.println(jsonString);

        //再反列化
        Map<String, Object> newMap = JacksonUtils.parseObject(jsonString);
        Set<String> set = newMap.keySet();
        List<String> newList = new ArrayList<>();
        for (String str : set){
            newList.add(str);
        }
        System.out.println("最终得到的list顺序为：");
        /*
            最终得到的顺序：可以看到和一开始的顺序已经不一致了，发生了乱序
            a1
            a
            b
            c
            d
            b1
         */
        for (String str : newList){
            System.out.println(str);
        }

    }

}
