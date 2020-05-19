package cn.com.kun.foo.powerMock.service.impl;

import cn.com.kun.foo.powerMock.po.MockModel;
import cn.com.kun.foo.powerMock.service.MockMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

//告诉JUnit使用PowerMockRunner进行测试
@RunWith(PowerMockRunner.class)
// 所有需要测试的类列在此处，适用于模拟final类或有final, private, static, native方法的类
//@PrepareForTest({MockUtil.class})
//为了解决使用powermock后，提示classloader错误
@PowerMockIgnore("javax.management.*")
public class MockServiceImplTest {

    //@InjectMocks的作用：将@Mock注解的示例也就是模拟对象注入进来
    //因为MockServiceImpl本来就注入了一个MockMapper
    //为了模拟，我们只能把虚构出来的模拟对象注入进去覆盖掉原来的dao层接口MockMapper，让它不要真正去请求数据库
    //不请求数据库的目的是为了用模拟数据进行模拟单测
    @InjectMocks
    private MockServiceImpl mockService;

    //用了@Mock注解，就表示这将会被改造成一个模拟对象
    @Mock
    private MockMapper mockMapper;//dao层接口

    /**
     * mock普通方法
     */
    @Test
    public void count() {

        //一个数据库实体类
        MockModel model = new MockModel();

        //植入模拟过程，每当执行mockMapper.count(model) 这个逻辑，就模拟返回一个自定义的值
        //例如这里，我可以自定义模拟返回20或者2，最终并不会真正去访问这个dao层
        PowerMockito.when(mockMapper.count(model)).thenReturn(2);

        //假如模拟返回2，和2做断言判断，单侧就是通过的
        //断言用的是JUnit的Assert
        Assert.assertEquals(2, mockService.count(model));
    }

}


