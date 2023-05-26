package cn.com.kun.component.logDesensitization.other;

import cn.com.kun.component.logDesensitization.LogDesensitizationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 这个类不建议放入组件
 *
 * author:xuyaokun_kzx
 * date:2023/5/23
 * desc:
*/
@RequestMapping("/log-desensitization")
@RestController
public class DesensitizationHelpController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DesensitizationHelpController.class);

    @CrossOrigin(value = "*") //这是正常的
//    @CrossOrigin(value = "http://localhost:8081") //这是正常的
    @PostMapping("/decrypt")
    public LogDesensitizationRes decrypt(@RequestBody LogDesensitizationReq req){

        LogDesensitizationRes res = new LogDesensitizationRes();
        res.setSource(req.getSource());
        res.setRealSource(LogDesensitizationUtils.decrypt(req.getSource()));
        return res;
    }

}
