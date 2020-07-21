package cn.com.kun.foo.powerMock.service;

import cn.com.kun.base.BaseTest;
import cn.com.kun.util.CustomMockUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MyPowerMockServiceTest extends BaseTest {

    @Autowired
    private MyPowerMockService myPowerMockService;

    @Value("${nbaplay.level}")
    private String nbaplayLevel;

    @Test
    public void method() {

        System.out.println(nbaplayLevel);
        myPowerMockService.method();
        CustomMockUtils.sleepRun();

    }


}