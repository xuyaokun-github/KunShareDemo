package cn.com.kun.controller;

import cn.com.kun.common.annotation.DesensitizationAnnotation;
import cn.com.kun.common.annotation.SecretAnnotation;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.PeopleReq;
import cn.com.kun.common.vo.PeopleRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/secret")
@RestController
public class SecretDemoController {

    public final static Logger logger = LoggerFactory.getLogger(SecretDemoController.class);

    //可以同时加密解密，针对入参解密，针对返回做加密
    @SecretAnnotation(decode = true) //表示需要解密
    @PostMapping("/testSecret")
    public PeopleRes testSecret(@RequestBody PeopleReq peopleReq){

        logger.info("控制层获取到的入参：{}", JacksonUtils.toJSONString(peopleReq));
        PeopleRes peopleRes = new PeopleRes();
        logger.info("控制层返回：{}", JacksonUtils.toJSONString(peopleRes));
        return peopleRes;
    }

    @SecretAnnotation(encode = true) //表示需要加密
    @GetMapping("/testEncode")
    public PeopleRes testEncode(){

        PeopleRes peopleRes = new PeopleRes();
        peopleRes.setIdCard("5552585556565");
        peopleRes.setIdCardRisk("44092619811023006X");
        logger.info("控制层返回：{}", JacksonUtils.toJSONString(peopleRes));
        return peopleRes;
    }

    @DesensitizationAnnotation //表示需要脱敏
    @SecretAnnotation(encode = true) //表示需要加密
    @GetMapping("/testDesensitization")
    public PeopleRes testDesensitization(){

        PeopleRes peopleRes = new PeopleRes();
        peopleRes.setIdCard("44092619811023006X");
        peopleRes.setIdCardRisk("44092619811023006X");
        logger.info("控制层返回：{}", JacksonUtils.toJSONString(peopleRes));
        return peopleRes;
    }

    /**
     * 有些时候，返回给前端的对象是带返回码，里面的body才是需要做加解密脱敏的
     * 这种情况如何处理？
     * @return
     */
    @DesensitizationAnnotation //表示需要脱敏
    @SecretAnnotation(encode = true) //表示需要加密
    @GetMapping("/testDesensitization")
    public PeopleRes testDesensitization2(){

        PeopleRes peopleRes = new PeopleRes();
        peopleRes.setIdCard("44092619811023006X");
        peopleRes.setIdCardRisk("44092619811023006X");
        logger.info("控制层返回：{}", JacksonUtils.toJSONString(peopleRes));
        return peopleRes;
    }
}
