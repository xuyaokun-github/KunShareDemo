package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.Map;

/**
 * 换行符问题
 * author:xuyaokun_kzx
 * date:2022/6/14
 * desc:
*/
public class TestHuangHangFu {

    public static void main(String[] args) {

        //value中有换行符，会报错，需要用上 JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS
//        String source = "{\"a1\":\"v1\",\"a3\":\"v3kkkk\n\",\"a2\":\"v2\"}";
        //假如value中有双引号，会报错
        String source = "{\"a1\":\"v1\",\"a3\":\"v3kkkk\"\"\",\"a2\":\"v2\"}";
        Map<String, Object> map = JacksonUtils.toMap(source);
        System.out.println(JacksonUtils.toJSONString(map));
    }



}
