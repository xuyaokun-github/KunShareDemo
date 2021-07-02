package cn.com.kun.foo.javacommon.reflect;


import java.time.LocalDate;

public class FooTeacher implements FooPeople {
    @Override
    public String work() {
        System.out.println("上课=>" + LocalDate.now() );
        return "OK";
    }
}