package cn.com.kun.foo.javacommon.lambda;

import java.util.Arrays;

import static java.util.stream.Collectors.averagingInt;

public class TestStream {

    public static void main(String[] args) {

//        List<Student> studentList = new ArrayList<>();
//        Student student = new Student();
//        student.setId(1L);
//        student.setStudentName("aaa");
//        Student student2 = new Student();
//        student2.setId(1L);
//        student2.setStudentName("bbb");
//        studentList.add(student);
//        studentList.add(student2);
//
//        Map<Long, String> map = studentList.stream().collect(Collectors.toMap(Student::getId, Student::getStudentName));
//        System.out.println(JacksonUtils.toJSONString(map));

        Arrays.asList(1, 2, 3, 4)
                .stream().collect(averagingInt(Integer::intValue));

    }

}
