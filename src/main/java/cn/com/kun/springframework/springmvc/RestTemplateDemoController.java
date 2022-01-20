package cn.com.kun.springframework.springmvc;

import cn.com.kun.bean.model.StudentReqVO;
import cn.com.kun.common.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/restTemplate-demo")
@RestController
public class RestTemplateDemoController {

    @Autowired
    private RestTemplate restTemplate;

    private String targetUrl = "http://127.0.0.1:8091/kunwebdemo/student/update";

    private String ownTargetUrl = "http://127.0.0.1:8080/kunsharedemo/restTemplate-demo/add";

    @GetMapping("/test")
    public Object test(){

        StudentReqVO studentReqVO = new StudentReqVO();
        studentReqVO.setId(8L);
        studentReqVO.setAddress("shen zhen");
        studentReqVO.setIdCard("10086");
        HttpEntity<?> httpEntity = new HttpEntity<>(studentReqVO);
        ResponseEntity responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST, httpEntity, Object.class);
        return responseEntity.getBody();
    }

    @GetMapping("/testInvokeOwn")
    public Object testInvokeOwn(){

        StudentReqVO studentReqVO = new StudentReqVO();
        studentReqVO.setId(8L);
        studentReqVO.setAddress("shen zhen");
        studentReqVO.setIdCard("10086");
        HttpEntity<?> httpEntity = new HttpEntity<>(studentReqVO);
        ResponseEntity responseEntity = restTemplate.exchange(ownTargetUrl, HttpMethod.POST, httpEntity, Object.class);
        return responseEntity.getBody();
    }

    @PostMapping("/add")
    public StudentReqVO add(@RequestBody StudentReqVO studentReqVO){

       return new StudentReqVO();
    }

    /**
     * 和上面的方法做对比，假如直接传字符串，
     * 在服务端那边会报错：org.springframework.web.HttpMediaTypeNotSupportedException: Content type 'text/plain;charset=ISO-8859-1' not supported
     * 它会默认用text/plain;charset=ISO-8859-1类型
     *
     * 假如传实体类，它会自动采用json的方式做转换，使用的Content type将是 application/json
     * @return
     */
    @GetMapping("/test2")
    public Object test2(){

        StudentReqVO studentReqVO = new StudentReqVO();
        studentReqVO.setId(8L);
        studentReqVO.setAddress("shen zhen");
        studentReqVO.setIdCard("10086");
        HttpEntity<?> httpEntity = new HttpEntity<>(JacksonUtils.toJSONString(studentReqVO));
        ResponseEntity responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST, httpEntity, Object.class);
        return responseEntity.getBody();
    }
}
