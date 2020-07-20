//package cn.com.kun.mapper;
//
//import cn.com.kun.KunShareDemoApplicationTest;
//import cn.com.kun.common.vo.User;
//import cn.com.kun.dataSet.MyXmlDataSetLoader;
//import com.github.springtestdbunit.annotation.DbUnitConfiguration;
//import org.dbunit.database.DatabaseConnection;
//import org.dbunit.database.IDatabaseConnection;
//import org.dbunit.database.QueryDataSet;
//import org.dbunit.operation.DatabaseOperation;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 基于测试数据库的单测
// *
// * Created by xuyaokun On 2020/6/21 14:59
// * @desc:
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = KunShareDemoApplicationTest.class)
////基于测试数据库获取模拟数据，不需要DataSetLoader
//public class UserMapperTest4 extends AbstractTransactionalJUnit4SpringContextTests {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    //引入工程的主数据源（因为例子中的数据库层是访问主数据源）
//    @Autowired
//    private DataSource dataSource;
//
//    //注入自定义的测试数据源，用的是不同的数据库
//    @Autowired
//    @Qualifier("testDataSource")
//    private DataSource testDataSource;
//
//    @Value("${test.config}")
//    private String str;
//
//    //注意：假如test和main下都有application.properties文件（同名的属性文件）
//    //会以test的为准，main下的不会加载，所以注入会失败导致单测失败
//    // nbaplay.level定义在src\main\resources\application.properties
////    @Value("${nbaplay.level}")
//    private String nbaplayLevel;
//
//    @Before
//    public void before() throws Exception{
//
//        //准备模拟数据
//        DatabaseConnection conn = new DatabaseConnection(DataSourceUtils.getConnection(testDataSource));
//        IDatabaseConnection connection = conn;
//        QueryDataSet dataSet = new QueryDataSet(connection);
//        dataSet.addTable("tbl_user", "select * from tbl_user");
//
//        IDatabaseConnection connection2 = new DatabaseConnection(DataSourceUtils.getConnection(dataSource));
//        DatabaseOperation.CLEAN_INSERT.execute(connection2, dataSet);
//    }
//
//    @Test
//    public void testQuery(){
//
//        System.out.println(str);
//        Map<String, String> map = new HashMap<>();
//        map.put("email", "999@qq.com");
//        List<User> actualUserList = userMapper.query(map);
//        //断言(校验请求回来的数据 和预期的数据是否一致)
//        Assert.assertNotEquals(null, actualUserList);
//
//    }
//
//
//}
