package cn.com.kun.springframework.springcloud.hystrix.controller;

import cn.com.kun.springframework.springcloud.hystrix.service.HystrixDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hystrix")
@RestController
public class HystrixDemoController {

    @Autowired
    private HystrixDemoService hystrixDemoService;

    @RequestMapping("/test1")
    public String testHello(){

        String res = hystrixDemoService.method1();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test2")
    public String test2(){

        String res = hystrixDemoService.method2();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test3")
    public String test3(){

        String res = hystrixDemoService.method3();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test4")
    public String test4(){

        String res = hystrixDemoService.method4();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test5")
    public String test5(){

        String res = hystrixDemoService.method5();
        System.out.println(res);
        return res;
    }


}
