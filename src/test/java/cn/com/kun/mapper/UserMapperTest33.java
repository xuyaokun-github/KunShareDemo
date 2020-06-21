package cn.com.kun.mapper;

import cn.com.kun.base.BaseXmlDBUnit;
import cn.com.kun.common.vo.User;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by xuyaokun On 2020/6/21 20:38
 * @desc: 
 */
public class UserMapperTest33 extends BaseXmlDBUnit {

    @Autowired
    private UserMapper userMapper;

    /**
     * 清空数据插入准备的数据
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //初始化准备数据
        setUp("testDataBak/tbl_user_testdata3.xml");
    }

    @Test
    public void selectList() {
        //调用数据库层方法
        Map<String, String> map = new HashMap<>();
        map.put("email", "userTwo@heardfate.com");
        List<User> actualUserList = userMapper.query(map);
        User user = actualUserList.get(0);
        //断言(校验请求回来的数据 和预期的数据是否一致)
        Assert.assertNotEquals(null, actualUserList);
    }



}


