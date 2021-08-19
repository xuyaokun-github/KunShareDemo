package cn.com.kun.springframework.core.ioc.thirdCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验证bean加载的三级缓存解决循环依赖
 *
 * author:xuyaokun_kzx
 * date:2021/8/18
 * desc:
*/
@Service
public class ThirdCacheDemoAService {

    @Autowired
    ThirdCacheDemoBService thirdCacheDemoBService;

    public void method(){

    }
}
