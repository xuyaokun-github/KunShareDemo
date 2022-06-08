package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.HashMap;
import java.util.Map;

public class TestJsonOrder {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("a1", "v1");
        map.put("a3", "v3");
        map.put("a2", "v2");
        //很明显，输出的字符串key顺序和put时的顺序并不一致
        //{"a1":"v1","a2":"v2","a3":"v3"}
        System.out.println(JacksonUtils.toJSONString(map));

        /*
            实验可见，不同的来源字符串，导致最终得到的json字符串中的key顺序不一致
            因此，很可能上游在送这个源串时，顺序就已经搞混乱了
         */
        String source2 = "{\"a1\":\"v1\",\"a3\":\"v3\",\"a2\":\"v2\"}";
        String source3 = "{\"a2\":\"v2\",\"a3\":\"v3\",\"a1\":\"v1\"}";

        Map<String, Object> map2 = JacksonUtils.toMap(source2);
        //{"a1":"v1","a3":"v3","a2":"v2"}
        System.out.println(JacksonUtils.toJSONString(map2));
        /*
        输出结果：
        a1:v1
        a3:v3
        a2:v2
         */
        showMap(map2);
        Map<String, Object> map3 = JacksonUtils.toMap(source3);
        //{"a2":"v2","a3":"v3","a1":"v1"}
        System.out.println(JacksonUtils.toJSONString(map3));
        /*
        输出结果：
        a2:v2
        a3:v3
        a1:v1
         */
        showMap(map3);


    }

    private static void showMap(Map<String, Object> map2) {

        map2.keySet().forEach(key->{
            System.out.println(key + ":" + map2.get(key));
        });
    }


}
