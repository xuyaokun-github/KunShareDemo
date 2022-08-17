package cn.com.kun.controller.other;

import cn.com.kun.bean.api.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.com.kun.bean.api.ResultCode.STUDENT_AND_CLASS_NOT_FOUND;
import static cn.com.kun.bean.api.ResultCode.STUDENT_NOT_FOUND;

@RestController
@RequestMapping("/result-demo")
public class ResultVODemoController {

    @GetMapping("/test1")
    public BaseResponse test1(){

        BaseResponse baseResponse = BaseResponse.error(STUDENT_NOT_FOUND, "888");
        return baseResponse;
    }

    @GetMapping("/test2")
    public BaseResponse test2(){

        BaseResponse baseResponse = BaseResponse.error(STUDENT_AND_CLASS_NOT_FOUND, "999", "777");
        return baseResponse;
    }

}
