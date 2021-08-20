package cn.com.kun.service;

import cn.com.kun.common.vo.ResultVo;

import java.util.Map;

public interface RateLimitDemoService {

    ResultVo method(Map<String, String> paramMap, String sendChannel);

    ResultVo method2(Map<String, String> paramMap, String sendChannel);

}
