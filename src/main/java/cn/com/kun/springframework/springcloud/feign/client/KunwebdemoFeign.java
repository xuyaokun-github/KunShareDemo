package cn.com.kun.springframework.springcloud.feign.client;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.bean.model.people.People;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 展示一个不走注册中心的例子
 * 这个模拟的就是调用第三方接口的场景
 * author:xuyaokun_kzx
 * date:2021/5/26
 * desc:
*/
@FeignClient(name = "kunwebdemo", url = "http://127.0.0.1:8091")
//@FeignClient(name = "kunwebdemo") //假如不指定url,则走注册中心
public interface KunwebdemoFeign {

    /**
     * Get请求-不带参数
     * @return
     */
//    @GetMapping("/feigndemo/test") //错误示例，因为少了上下文，服务端那边定义了上下文，则接口这里必须加上下文
    @GetMapping("/kunwebdemo/feigndemo/test")
    ResultVo result();

    /**
     * Get请求-带参数
     * @param name
     * @return
     */
    @GetMapping("/kunwebdemo/feigndemo/test1")
    ResultVo result1(@RequestParam("name") String name);

    /**
     * Post请求-带参数
     * @param name
     * @return
     */
    @PostMapping("/kunwebdemo/feigndemo/test2")
    ResultVo result2(@RequestBody People name);

    /**
     * Post请求-带参数+headers
     * 亲测这样是能成功传递headers的
     * @param name
     * @return
     */
    @PostMapping("/kunwebdemo/feigndemo/test3")
    ResultVo result3(@RequestBody People name,
                     @RequestHeader(value = "source", required = false) String source);


}
