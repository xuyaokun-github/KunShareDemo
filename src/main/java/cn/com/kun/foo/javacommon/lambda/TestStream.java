package cn.com.kun.foo.javacommon.lambda;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.common.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestStream {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setId(1L);
        student.setStudentName("aaa");
        Student student2 = new Student();
        student2.setId(1L);
        student2.setStudentName("bbb");
        studentList.add(student);
        studentList.add(student2);

        Map<Long, String> map = studentList.stream().collect(Collectors.toMap(Student::getId, Student::getStudentName));
        System.out.println(JacksonUtils.toJSONString(map));
    }

}
