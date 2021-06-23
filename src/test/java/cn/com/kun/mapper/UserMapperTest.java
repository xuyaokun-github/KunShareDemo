//package cn.com.kun.mapper;
//
//import cn.com.kun.bean.entity.User;
//import cn.com.kun.foo.springtestdbunit.BaseDbunitTestCase;
//import org.dbunit.DatabaseUnitException;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.sql.DataSource;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// *
// * Created by xuyaokun On 2020/6/18 23:36
// * @desc:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserMapperTest extends BaseDbunitTestCase {
//
//    private User expectedUser;//期望返回的数据
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    public UserMapperTest() throws DatabaseUnitException {
//
//        //指定一个全局路径也可以，指定resource下的路径也可以
//        super("classpath:testData/tbl_user_testdata.xlsx");
//    }
//
//    @Before                   //前置通知
//    public void init() throws SQLException, DatabaseUnitException, IOException {
//
//        //关于单测的期望数据，不一定要放在@Before，可以放在一个统一的地方，在运行单测时再去获取
//        //例如公司可以搭建一个统一的单测平台，提供测试用例，例如可以用HTTP的方式获取测试用例
//        //测试用例包括期望数据，请求数据，返回数据，Mock数据等
//        expectedUser = new User();
//        expectedUser.setEmail("nba@qq.com");
//
//        //初始化数据库的测试用例
//        initDbunitTestCase(dataSource.getConnection(), "tbl_user");
//    }
//
//
//    @Test
//    public void insert() {
//    }
//
//    @Test
//    public void update() {
//    }
//
//    @Test
//    public void query() {
//    }
//
//    @Test
//    public void deleteByFirstname() {
//    }
//
//    @Test
//    public void testQuery(){
//
//        //调用数据库层方法
//        Map<String, String> map = new HashMap<>();
//        map.put("email", "nba@qq.com");
//        List<User> actualUserList = userMapper.query(map);
//        User user = actualUserList.get(0);
//        //断言(校验请求回来的数据 和预期的数据是否一致)
//        Assert.assertNotEquals(null, actualUserList);
//        Assert.assertEquals(expectedUser.getEmail(), user.getEmail());
//
//    }
//
//
//    /**
//     * 还原数据
//     */
//    @After
//    public void destory() throws FileNotFoundException, DatabaseUnitException, SQLException {
//        //还原数据库表的数据
//        resumeTable();
//    }
//}