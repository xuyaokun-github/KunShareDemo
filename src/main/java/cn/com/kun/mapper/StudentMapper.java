package cn.com.kun.mapper;

import cn.com.kun.bean.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    int insert(Student student);

    int update(Student student);

    List<Student> query(Map map);

    /**
     * 查单个学生
     * @param id
     * @return
     */
    Student getStudentById(@Param("id") Long id);

    /**
     * 物理删除
     * @param id
     * @return
     */
    int delete(@Param("id") Long id);
}

