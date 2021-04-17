//package cn.com.kun.mapper;
//
//import cn.com.kun.KunShareDemoApplicationTest;
//import cn.com.kun.common.entity.User;
//import cn.com.kun.dataSet.MyXlsDataSetLoader;
//import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
//import com.github.springtestdbunit.annotation.DatabaseSetup;
//import com.github.springtestdbunit.annotation.DbUnitConfiguration;
//import com.github.springtestdbunit.annotation.ExpectedDatabase;
//import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestExecutionListeners;
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
//@TestExecutionListeners({
//        DependencyInjectionTestExecutionListener.class,
//        TransactionDbUnitTestExecutionListener.class})
////指定使用自定义的数据加载器MyXlsDataSetLoader
//@DbUnitConfiguration(dataSetLoader = MyXlsDataSetLoader.class)
////必须开启事务，否则清空数据库的操作就是真正生效
////这里事务的目的是为了让测试临时生效，单测跑完就回滚
//@Transactional
//public class UserMapperTest2 {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Test
//    //指定路径是在\src\test\resources下
//    @DatabaseSetup({"/testDataBak/tbl_user_testdata.xlsx"})
//    public void testQuery(){
//
//        //调用数据库层方法
//        Map<String, String> map = new HashMap<>();
//        map.put("email", "nba@qq.com");
//        List<User> actualUserList = userMapper.query(map);
//        User user = actualUserList.get(0);
//        //断言(校验请求回来的数据 和预期的数据是否一致)
//        Assert.assertNotEquals(null, actualUserList);
//
//    }
//
//
//    @Test
//    //指定路径是在\src\test\resources下
//    @DatabaseSetup({"/testDataBak/tbl_user_testdata1.xlsx"})
//    //assertionMode指定断言模式，假如Excel里的数据只填了单测的表，则必须设置模式为非严格模式，否则抛异常
//    @ExpectedDatabase(value = "/testDataBak/tbl_user_testdata2.xlsx", assertionMode = DatabaseAssertionMode.NON_STRICT)
//    public void testDelete(){
//
//        String firstName = "zhangxueyou";
//        //调用数据库层方法，做删除
//        userMapper.deleteByFirstname(firstName);
//        //因为用了@ExpectedDatabase注解做断言，所以不需要再加额外的断言
//
//    }
//
//}
