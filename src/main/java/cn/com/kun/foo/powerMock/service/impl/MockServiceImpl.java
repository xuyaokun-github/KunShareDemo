package cn.com.kun.foo.powerMock.service.impl;

import cn.com.kun.foo.powerMock.po.MockModel;
import cn.com.kun.foo.powerMock.service.MockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockServiceImpl {

//    @Autowired
    private MockMapper mockMapper;

    public int count(MockModel model) {
        return mockMapper.count(model);
    }

}
