package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.JacksonUtils;

public class TestJackson {

    public static void main(String[] args) {

        JacksonVO jacksonVO = new JacksonVO();
        jacksonVO.setAAA("111");

        //得到 {"aaa":"111"}
        System.out.println(JacksonUtils.toJSONString(jacksonVO));
        //加了注解之后 {"AAA":"111"}
        System.out.println(JacksonUtils.toJSONString(jacksonVO));
    }
}
