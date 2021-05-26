package cn.com.kun.springframework.springcloud.feign.service;

import cn.com.kun.common.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "kunshare-eureka-client-one")
public interface KunShareClientOneFeignService {

    /**
     * 对应kunshare-eureka-client-one应用的/feign/v1/result接口
     * @return
     */
    @GetMapping("/feign/v1/result")
    ResultVo result();


}
