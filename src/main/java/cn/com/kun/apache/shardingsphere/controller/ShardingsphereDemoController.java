package cn.com.kun.apache.shardingsphere.controller;

import cn.com.kun.common.entity.Student;
import cn.com.kun.common.entity.User;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuyaokun On 2020/11/5 23:12
 * @desc: 
 */
@RequestMapping("/shardingsphere")
@RestController
public class ShardingsphereDemoController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/students/insert")
    public String testString(){

        Student student = new Student();
        student.setIdCard("" + System.currentTimeMillis());
        student.setStudentName("name-" + student.getIdCard());
        student.setAddress("address-" + student.getIdCard());
        studentMapper.insert(student);
        return "kunghsu";
    }

    @RequestMapping("/students/query")
    public String query(){

        Map<String, String> map = new HashMap<>();
        map.put("idCard", "1604590836961");
        List<Student> studentList = studentMapper.query(map);

        return "" + studentList.size();
    }


    @RequestMapping("/user/insert")
    public String insert(){

        User user = new User();
        user.setFirstname("123");
        user.setLastname("name");
        userMapper.insert(user);
        return "success";
    }


}
