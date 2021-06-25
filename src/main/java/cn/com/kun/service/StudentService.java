package cn.com.kun.service;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.vo.ResultVo;

public interface StudentService {


    ResultVo<StudentResVO> getStudentById(Long id);

    ResultVo<Integer> add(StudentReqVO reqVO);

    ResultVo<Integer> update(StudentReqVO reqVO);

    ResultVo<Integer> delete(Long id);

}
