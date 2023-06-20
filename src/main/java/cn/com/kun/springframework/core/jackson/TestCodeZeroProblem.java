package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.Map;

/**
 * 测试转义情况
 *
 * 解决办法：
 *         //允许字符串中存在回车换行控制符
 *         mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
 *
 * author:xuyaokun_kzx
 * date:2022/6/10
 * desc:
*/
public class TestCodeZeroProblem {

    public static void main(String[] args) {

        String source = "{\"a1\":\"v1\",\"a3\":\"kunghsu\",\"a2\":\"v2\"}";
        Map<String, Object> map = JacksonUtils.toMap(source);
        System.out.println(JacksonUtils.toJSONString(map));
        showMap(map);

        //开始制造问题
        System.out.println("---------------------------------");
        source = source.replace("kunghsu", "xyk" + ((char)0));
        map = JacksonUtils.toMap(source);
        System.out.println(JacksonUtils.toJSONString(map));
        showMap(map);
        String source2 = JacksonUtils.toJSONString(map);
        map = JacksonUtils.toMap(source2);
        showMap(map);

    }

    private static void showMap(Map<String, Object> map2) {

        map2.keySet().forEach(key->{
            System.out.println(key + ":" + map2.get(key));
        });
    }


}
