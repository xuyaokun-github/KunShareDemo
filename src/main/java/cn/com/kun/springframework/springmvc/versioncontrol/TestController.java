package cn.com.kun.springframework.springmvc.versioncontrol;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本控制demo
 *
 * author:xuyaokun_kzx
 * date:2023/8/3
 * desc:
*/
@RestController
@ApiVersion
@RequestMapping(value = "/spring-mvc-version-control/{version}/test")
public class TestController {

    /**
     * http://localhost:8080/kunsharedemo/spring-mvc-version-control/v1.0/test/one
     * @return
     */
    @GetMapping(value = "one")
    public String query(){
        return "test api default";
    }

    /**
     * http://localhost:8080/kunsharedemo/spring-mvc-version-control/v1.1/test/one
     * @return
     */
    @GetMapping(value = "one")
    @ApiVersion("1.1")
    public String query2(){
        return "test api v1.1";
    }

    @GetMapping(value = "one")
    @ApiVersion("3.1")
    public String query3(){
        return "test api v3.1";
    }

}
