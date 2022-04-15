package cn.com.kun.foo.javacommon.lambda;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

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


        Map<String, String> map = Arrays.asList(1, 2, 3, 4).stream().collect(Collectors.toMap(String::valueOf, String::valueOf));

//        map.keySet().stream().filter(key-> (Integer.valueOf(key)>2)).forEach(key ->{
//            map.remove(key);
//        });

        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            if (Integer.valueOf(key)>2){
                Object obj = map.get(key);
                iterator.remove();
            }
        }

//        map.entrySet().removeIf(entry -> entry.getValue() % 2 == 0);

        System.out.println(map);

        //filter 为true的则保留
//        map.keySet().stream().filter(key-> (Integer.valueOf(key)>2)).forEach(key ->{
//            System.out.println(key);
//        });

        System.out.println(map);
    }

}
