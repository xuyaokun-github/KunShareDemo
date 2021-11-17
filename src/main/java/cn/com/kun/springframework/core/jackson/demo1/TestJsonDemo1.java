package cn.com.kun.springframework.core.jackson.demo1;

import cn.com.kun.common.utils.JacksonUtils;

public class TestJsonDemo1 {

    public static void main(String[] args) {


        UserJsonVO1 userJsonVO1 = new UserJsonVO1();
        userJsonVO1.setName("kunghsu");
        userJsonVO1.setName1("xyk");
        String source = JacksonUtils.toJSONString(userJsonVO1);
        System.out.println(source);
        UserJsonVO2 userJsonVO2 = JacksonUtils.toJavaObject(source, UserJsonVO2.class);
        System.out.println(userJsonVO2);
        System.out.println(JacksonUtils.toJSONString(userJsonVO2));

    }

}
