package cn.com.kun.springframework.core.jackson.demo1;

import cn.com.kun.common.utils.JacksonUtils;

public class TestJsonProperty {

    public static void main(String[] args) {

        UserJsonVO3 userJsonVO3 = new UserJsonVO3();
        userJsonVO3.setName("kunghsu");
        System.out.println(JacksonUtils.toJSONString(userJsonVO3));
    }
}
