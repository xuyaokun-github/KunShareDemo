package cn.com.kun.framework.quartz.service;

import cn.com.kun.util.CustomMockUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class) // 告诉JUnit使用PowerMockRunner进行测试
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MyQuartzDemoServiceTest {

    @Test
    public void method() {
        CustomMockUtils.sleepRun();
    }

    @Test
    public void method2() {
        CustomMockUtils.sleepRun();
    }


}