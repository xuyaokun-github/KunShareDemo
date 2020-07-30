package cn.com.kun.foo.powerMock.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
@SpringBootTest
public class MySecondNbaServiceTest {

    //两个注解最好加上
    //@InjectMocks注解负责注入模拟对象
    @InjectMocks
    //当MyNbaService类中还有其他被spring管理的bean,@Autowired必须加，因为power无法注入spring的bean，
    @Autowired
    MyNbaService myNbaService;

    @Mock
    RocketService rocketService;

    @Test
    public void play() {

        PowerMockito.when(rocketService.sayRocket()).thenReturn("mockString");
        String result = myNbaService.playByRocket();
        System.out.println(result);
        //返回值是mockString，说明打桩成功，调用的就是打桩的模拟方法
        Assert.assertEquals("mockString", result);
    }


}