//package cn.com.kun.mapper;
//
//import cn.com.kun.KunShareDemoApplicationTest;
//import cn.com.kun.common.entity.User;
//import cn.com.kun.dataSet.MyXmlDataSetLoader;
//import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
//import com.github.springtestdbunit.annotation.DatabaseSetup;
//import com.github.springtestdbunit.annotation.DbUnitConfiguration;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestExecutionListeners;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// *
// * Created by xuyaokun On 2020/6/21 14:59
// * @desc:
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = KunShareDemoApplicationTest.class)
////不同类使用不同的DataSetLoader
////本类指定使用自定义的数据加载器MyXmlDataSetLoader
//@DbUnitConfiguration(dataSetLoader = MyXmlDataSetLoader.class)
//public class UserMapperTest3 extends AbstractTransactionalJUnit4SpringContextTests {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Test
//    //指定路径是在\src\test\resources下
//    @DatabaseSetup({"/testDataBak/tbl_user_testdata3.xml"})
//    public void testQuery(){
//
//        Map<String, String> map = new HashMap<>();
//        map.put("email", "userTwo@heardfate.com");
//        List<User> actualUserList = userMapper.query(map);
//        //断言(校验请求回来的数据 和预期的数据是否一致)
//        Assert.assertNotEquals(null, actualUserList);
//
//    }
//
//
//}
