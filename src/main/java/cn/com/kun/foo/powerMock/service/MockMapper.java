package cn.com.kun.foo.powerMock.service;

import cn.com.kun.foo.powerMock.po.MockModel;

/**
 * 一个Dao层，待会将是被Mock的对象
 * 用Mock对象替换掉真正的Dao层
 */
public interface MockMapper {

    int count(MockModel model);

}
