package cn.com.kun.foo.powerMock.service.impl;

import cn.com.kun.util.CustomMockUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class) // 告诉JUnit使用PowerMockRunner进行测试
@PrepareForTest({MyObjectServiceImpl.class}) // 所有需要测试的类列在此处，适用于模拟final类或有final, private, static, native方法的类
@PowerMockIgnore("javax.management.*") //为了解决使用powermock后，提示classloader错误
public class MyObjectServiceImplTest {

    @InjectMocks
    private MyObjectServiceImpl mockService;

    @Test
    public void makeFile() {

        Assert.assertFalse(mockService.makeFile("abc"));
    }

    @Test
    public void test1() {
        CustomMockUtils.sleepRun();
    }

    @Test
    public void test2() {
        CustomMockUtils.sleepRun();
    }
}