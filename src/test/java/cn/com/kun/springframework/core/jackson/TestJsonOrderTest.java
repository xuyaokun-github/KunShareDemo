package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestJsonOrderTest {

    @Test
    public void main() {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("a1", "");
        paramMap.put("b2", "");
        paramMap.put("c3", "");
        paramMap.put("b4", "");
        paramMap.put("j", "");
        paramMap.put("k", "");
        paramMap.put("l", "");
        paramMap.put("A1", "");
        paramMap.put("A2", "");
        paramMap.put("A3", "");

        String messageContent = JacksonUtils.toJSONString(paramMap);

        Map<String, Object> map = new HashMap<>();
        map.put("messageContent", messageContent);
        Map<String, String> channelMap = new HashMap<>();
        channelMap.put("template1", "a1,b2,c3,c4");
        channelMap.put("template2", "A1,A2,A3");
        map.put("smsTemplateParamOrder", channelMap);

        System.out.println(JacksonUtils.toJSONString(map));
    }


    @Test
    public void main2() {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("template1_1", "");
        paramMap.put("template1_2", "");
        paramMap.put("template1_3", "");
        paramMap.put("template1_4", "");
        paramMap.put("j", "");
        paramMap.put("k", "");
        paramMap.put("l", "");
        paramMap.put("template2_1", "");
        paramMap.put("template2_2", "");
        paramMap.put("template2_3", "");

        String messageContent = JacksonUtils.toJSONString(paramMap);

        Map<String, Object> map = new HashMap<>();
        map.put("messageContent", messageContent);

        System.out.println(JacksonUtils.toJSONString(map));
    }
}