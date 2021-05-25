package cn.com.kun.controller;

import cn.com.kun.common.annotation.DesensitizationAnnotation;
import cn.com.kun.common.annotation.SecretAnnotation;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.common.vo.people.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
     * 可以通过类型判断。
     * 假如嵌套不止一层，而是多个嵌套，例如ResultVo<ResultVo<ResultVo<PeopleRes>>>
     * 这种太繁杂，不做处理！假如要处理只能用递归，但是不推荐这种多层次的返回。
     *

     {
     "message": "处理成功",
     "value": {
        "firstname": null,
        "lastname": null,
        "phone": null,
        "email": null,
        "company": null,
        "idCard": "ecc425fdd9e711cc4ff8f89eb98120d68a5610f1f79918de37c5cfc4fba9e74d",
        "idCardRisk": "4409*****006X",
        "homeTownAddress": {
            "cityName": "深圳市",
            "streetName": "福田街**",
            "postcode": {
                "postcodeNum": "52589*****5412",
                "postcodeNumDesc": "福田区统一邮编"
            }
        }
     },
     "success": true,
     "msgCode": "000000",
     "resultMap": null
     }

     * @return
     */
    @DesensitizationAnnotation //表示需要脱敏
    @SecretAnnotation(encode = true) //表示需要加密
    @GetMapping("/testDesensitizationWithResultVo")
    public ResultVo<PeopleRes> testDesensitizationWithResultVo(){

        PeopleRes peopleRes = new PeopleRes();
        peopleRes.setIdCard("44092619811023006X");
        peopleRes.setIdCardRisk("44092619811023006X");
        HomeTownAddress homeTownAddress = new HomeTownAddress();
        homeTownAddress.setCityName("深圳市");
        homeTownAddress.setStreetName("福田街光明路29号");
        Postcode postcode = new Postcode();
        postcode.setPostcodeNum("52589636985412");
        postcode.setPostcodeNumDesc("福田区统一邮编");
        homeTownAddress.setPostcode(postcode);
        peopleRes.setHomeTownAddress(homeTownAddress);
        logger.info("testDesensitizationWithResultVo控制层返回：{}", JacksonUtils.toJSONString(peopleRes));
        return ResultVo.valueOfSuccess(peopleRes);
    }

    /**
        脱敏成功例子如下：
     {
     "message": "处理成功",
     "value": {
     "firstname": null,
     "lastname": null,
     "phone": null,
     "email": null,
     "company": null,
     "idCard": "ecc425fdd9e711cc4ff8f89eb98120d68a5610f1f79918de37c5cfc4fba9e74d",
     "idCardRisk": "4409*****006X",
     "homeTownAddress": {
     "cityName": "深圳市",
     "streetName": "福田街**",
     "postcode": {
     "postcodeNum": "52589*****5412",
     "postcodeNumDesc": "福田区统一邮编"
     }
     },
     "workAddressList": [
     {
     "cityName": "深圳市",
     "streetName": "罗湖街**",
     "postcode": {
     "postcodeNum": "52589*****5412",
     "postcodeNumDesc": "福田区统一邮编"
     }
     },
     {
     "cityName": "深圳市",
     "streetName": "宝安街**",
     "postcode": {
     "postcodeNum": "52589*****5412",
     "postcodeNumDesc": "福田区统一邮编"
     }
     },
     {
     "cityName": "深圳市",
     "streetName": "龙岗街**",
     "postcode": {
     "postcodeNum": "52589*****5412",
     "postcodeNumDesc": "福田区统一邮编"
     }
     }
     ]
     },
     "success": true,
     "msgCode": "000000",
     "resultMap": null
     }

     * @return
     */
    @DesensitizationAnnotation //表示需要脱敏
    @SecretAnnotation(encode = true) //表示需要加密
    @GetMapping("/testDesensitizationWithResultVo2")
    public ResultVo<PeopleRes> testDesensitizationWithResultVo2(){

        PeopleRes peopleRes = new PeopleRes();
        peopleRes.setIdCard("44092619811023006X");
        peopleRes.setIdCardRisk("44092619811023006X");
        HomeTownAddress homeTownAddress = new HomeTownAddress();
        homeTownAddress.setCityName("深圳市");
        homeTownAddress.setStreetName("福田街光明路29号");
        Postcode postcode = new Postcode();
        postcode.setPostcodeNum("52589636985412");
        postcode.setPostcodeNumDesc("福田区统一邮编");
        homeTownAddress.setPostcode(postcode);
        peopleRes.setHomeTownAddress(homeTownAddress);

        //
        List<WorkAddress> workAddressList = new ArrayList<>();
        WorkAddress workAddress = new WorkAddress();
        workAddress.setCityName("深圳市");
        workAddress.setStreetName("罗湖街幸福路29号");
        WorkAddress workAddress1 = new WorkAddress();
        workAddress1.setCityName("深圳市");
        workAddress1.setStreetName("宝安街幸福路29号");
        WorkAddress workAddress2 = new WorkAddress();
        workAddress2.setCityName("深圳市");
        workAddress2.setStreetName("龙岗街幸福路29号");
        Postcode postcode1 = new Postcode();
        postcode1.setPostcodeNum("52589636985412");
        postcode1.setPostcodeNumDesc("福田区统一邮编");
        workAddress.setPostcode(postcode1);
        workAddress1.setPostcode(postcode1);
        workAddress2.setPostcode(postcode1);
        workAddressList.add(workAddress);
        workAddressList.add(workAddress1);
        workAddressList.add(workAddress2);
        peopleRes.setWorkAddressList(workAddressList);

        logger.info("testDesensitizationWithResultVo2控制层返回：{}", JacksonUtils.toJSONString(peopleRes));
        return ResultVo.valueOfSuccess(peopleRes);
    }

}
