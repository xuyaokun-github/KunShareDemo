package cn.com.kun.springframework.core.jackson;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.people.People;

import java.util.Date;

public class TestJackson {

    public static void main(String[] args) {

        String source = JacksonUtils.toJSONString("kunghsu");
        System.out.println(source);//"kunghsu"
        System.out.println(JacksonUtils.parseObject(source, String.class));//kunghsu

//        testMethod1();
//        testMethod2();
        testMethod3();
    }

    private static void testMethod3() {

        //这是正常的字符串，反序列化是正常的（两个反斜杠会被认为是一个反斜杠）
        String source1 = "{\"company\":\"aaa\\\\sss\"}";
        People people = JacksonUtils.toJavaObject(source1, People.class);
        //这是有问题的字符串，反序列化会出问题（假如只有一个反斜杠，它就无法处理了）
        String source2 = "{\"company\":\"aaa\\sss\"}";
        /*
            反序列化异常，因为字符串里含有右斜杠
            所以这里得到的对象是空
         */
        source2 = source2.replace("\\", "");
        People people2 = JacksonUtils.toJavaObject(source2, People.class);
        System.out.println(people);
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
