package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.Map;

/**
 * 测试转义情况
 *
 * author:xuyaokun_kzx
 * date:2022/6/10
 * desc:
*/
public class TestZhuanYi {

    public static void main(String[] args) {

        String source = "{\"a1\":\"v1\",\"a3\":\"v3kkkk\\\"aaaa\\\"\",\"a2\":\"v2\"}";
        Map<String, Object> map = JacksonUtils.toMap(source);
        System.out.println(JacksonUtils.toJSONString(map));
        showMap(map);

    }

    private static void showMap(Map<String, Object> map2) {

        map2.keySet().forEach(key->{
            System.out.println(key + ":" + map2.get(key));
        });
    }


}
