package cn.com.kun.springframework.core.jackson.toStringDemo;

import cn.com.kun.common.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JacksonToStringDemo {

    public static void main(String[] args) {

        //类上加属性：@JsonInclude(JsonInclude.Include.NON_NULL)
        JsonIncludeDemoVo jsonIncludeDemoVo = new JsonIncludeDemoVo();
        jsonIncludeDemoVo.setName(UUID.randomUUID().toString());
        List<JsonIncludeDemoInner> innerList = new ArrayList<>();
        JsonIncludeDemoInner inner = new JsonIncludeDemoInner();
        inner.setCode1(UUID.randomUUID().toString());
        innerList.add(inner);
        jsonIncludeDemoVo.setInnerList(innerList);

        String targetString = JacksonUtils.toJSONString(jsonIncludeDemoVo);
        System.out.println(targetString);

        //反序列化
        JsonIncludeDemoVo jsonIncludeDemoVo2 = JacksonUtils.toJavaObject(targetString, JsonIncludeDemoVo.class);
        System.out.println();
    }

}
