package cn.com.kun.foo.powerMock.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
@SpringBootTest
public class MyNbaServiceTest {

    @Autowired
    MyNbaService myNbaService;

    @Autowired
    RocketService rocketService;

    @Test
    public void play() {

        MyNbaService myNbaServiceMock = PowerMockito.mock(MyNbaService.class);
        PowerMockito.when(myNbaServiceMock.play()).thenReturn("mockString");
        String result = myNbaServiceMock.play();
        System.out.println(result);
        //返回值是mockString，说明打桩成功，调用的就是打桩的模拟方法
        Assert.assertEquals("mockString", result);
    }

    //下面演示为一个service的属性打桩
    //这种写法，稍微麻烦一点
    @Test
    public void play1() {

        RocketService rocketServiceMock = PowerMockito.mock(RocketService.class);
        PowerMockito.when(rocketServiceMock.sayRocket()).thenReturn("mockString");
        //关键一步，要把Mock对象设置进目标service中，接下来要单测的是目标service
        ReflectionTestUtils.setField(myNbaService, "rocketService", rocketServiceMock);
        String result = myNbaService.playByRocket();
        System.out.println(result);
        //执行完之后，要把myNbaService还原，因为假如不还原，会影响其他测试用例
        //因为其他单测类里，可能有用到这个myNbaService
        ReflectionTestUtils.setField(myNbaService, "rocketService", rocketService);
        //返回值是mockString，说明打桩成功，调用的就是打桩的模拟方法
        Assert.assertEquals("mockString", result);
    }

}