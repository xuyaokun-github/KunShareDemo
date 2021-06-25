package cn.com.kun.controller;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.bean.model.StudentResVO;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/student")
@RestController
public class StudentController {

    public final static Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentService studentService;

    @PutMapping("/add")
    public ResultVo<Integer> add(@RequestBody StudentReqVO reqVO){
        return studentService.add(reqVO);
    }

    @PostMapping("/update")
    public ResultVo<Integer> update(@RequestBody StudentReqVO reqVO){
        return studentService.update(reqVO);
    }

    @DeleteMapping("/delete")
    public ResultVo<Integer> delete(@RequestParam("id") Long id){
        return studentService.delete(id);
    }

    @RequestMapping("/info")
    public ResultVo<StudentResVO> info(Long id){
        return studentService.getStudentById(id);
    }



}
