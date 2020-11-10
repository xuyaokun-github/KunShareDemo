package cn.com.kun.mapper;

import cn.com.kun.common.vo.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    void insert(Student student);

    void update(Student student);

    List<Student> query(Map map);

}
