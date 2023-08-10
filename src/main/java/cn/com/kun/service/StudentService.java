package cn.com.kun.service;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.vo.ResultVo;

import java.util.List;
import java.util.Map;

public interface StudentService {


    ResultVo<StudentResVO> getStudentById(Long id);

    ResultVo<Integer> add(StudentReqVO reqVO);

    ResultVo<Integer> update(StudentReqVO reqVO);

    ResultVo<Integer> delete(Long id);

    Student saveIfNotExist();

    Student saveIfNotExist2();

    List<Student> query(Map<String, Object> map);

    int save(Student student);

    int updateByIdCard(String address, String idCard);

    void updateByIdCard2(String toString, String idCard);

    int save2(Student student3);

    void saveBatch(List<Student> studentList);

}
