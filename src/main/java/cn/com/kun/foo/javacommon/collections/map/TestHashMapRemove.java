package cn.com.kun.foo.javacommon.collections.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestHashMapRemove {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");

        //反例
        try {
            Set<String> sets = map.keySet();
            for (String str : sets){
                if ("b".equals(str)){
                    map.remove(str);
                }
            }
            System.out.println(map.size());
        }catch (Exception e){
            e.printStackTrace();
        }


        //正例
        Iterator< Map.Entry < String, String >> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry < String, String > entry = it.next();
            String key = entry.getKey();
            if ("b".equals(key)) {
                it.remove();
            }
        }
        System.out.println(map.size());
    }



}
