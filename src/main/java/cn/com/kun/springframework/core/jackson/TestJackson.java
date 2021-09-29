package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;

import java.util.Date;

public class TestJackson {

    public static void main(String[] args) {

//        testMethod1();

        testMethod2();
    }

    private static void testMethod1() {
        JacksonVO jacksonVO = new JacksonVO();
        jacksonVO.setAAA("111");

        //得到 {"aaa":"111"}
        System.out.println(JacksonUtils.toJSONString(jacksonVO));
        //加了注解之后 {"AAA":"111"}
        System.out.println(JacksonUtils.toJSONString(jacksonVO));
    }

    private static void testMethod2() {

        Date date = new Date();
        System.out.println(DateUtils.toStr(date, DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS));
        JacksonVO2 jacksonVO2 = new JacksonVO2();
        jacksonVO2.setCreateTime(date);
        JacksonVO3 jacksonVO3 = new JacksonVO3();
        jacksonVO3.setCreateTime(date);
        System.out.println(JacksonUtils.toJSONString(jacksonVO2));
        System.out.println(JacksonUtils.toJSONString(jacksonVO3));
    }


}
