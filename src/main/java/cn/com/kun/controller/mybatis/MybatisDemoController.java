package cn.com.kun.controller.mybatis;

import cn.com.kun.bean.entity.Student;
import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.common.vo.user.UserQueryParam;
import cn.com.kun.controller.spring.SpringDemoController;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.mapper.UserMapper;
import cn.com.kun.service.mybatis.MybatisCursorDemoService;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cn.com.kun.common.utils.DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss_SSS;

@RequestMapping("/mybatis-demo")
@RestController
public class MybatisDemoController {

    private final static Logger logger = LoggerFactory.getLogger(SpringDemoController.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    private MybatisCursorDemoService mybatisCursorDemoService;

    @RequestMapping("/test")
    public String test(){

        String sql = "select * from tbl_user limit 1";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try (Connection conn = sqlSession.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);){
            ResultSet resultSet =  preparedStatement.executeQuery();
            while (resultSet.next()) {//循环判断下一个结果集是否为空
                System.out.println(resultSet.getString("firstname"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "kunghsu cn.com.kun.controller.mybatis.MybatisDemoController.test";
    }

    @RequestMapping("/test2")
    public String test2(){

        List<User> userList = userMapper.query(null);
        return JacksonUtils.toJSONString(userList);
    }

    /**
     * 测试分页查询
     * @return
     */
    @RequestMapping("/testPage")
    public String testPage(){

        int pageNum = 0;
        int pageSize = 5;
//        PageHelper.startPage(pageNum, pageSize);
        //倒序排序
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<User> userList = userMapper.query(null);
        return JacksonUtils.toJSONString(userList);
    }

    @RequestMapping("/testQuery")
    public String testQuery(@RequestBody UserQueryParam userQueryParam){
        logger.info("参数参数：{}", JacksonUtils.toJSONString(userQueryParam));
        List<User> userList = userMapper.list(userQueryParam);
        return JacksonUtils.toJSONString(userList);
    }

    @RequestMapping("/testCursor")
    public ResultVo testCursor(){
        return mybatisCursorDemoService.test1();
    }


    @RequestMapping("/testConn")
    public ResultVo testConn(){

        List<Connection> connectionList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            Connection conn = sqlSession.getConnection();
            connectionList.add(conn);
            logger.info("放入第{}个连接", i + 1);
        }

        return ResultVo.valueOfSuccess();
    }

    @RequestMapping("/testUpdate")
    public ResultVo testUpdate(){

        //正确的
//        SqlSession sqlSession = sqlSessionFactory.openSession(false);
//        SqlSession sqlSession = sqlSessionFactory.openSession();//假如不传，默认就是false

        //无法正常提交事务的
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection conn = sqlSession.getConnection();
        try {
            //关闭事务自动提交
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            logger.info("关闭事务自动提交异常", throwables);
        }

        Student student = new Student();
        student.setId(4L);
        student.setStudentName("123456" + System.currentTimeMillis());
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        int res = mapper.update(student);
        try {
            /*
            假如conn设置为false,则必须用conn的commit方法手动提交，
            否则就会在com.alibaba.druid.pool.DruidDataSource#recycle方法里执行回滚！
             */
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

//        sqlSession.commit(); //有没有这句，事务都会提交！因为是自动提交的！！

        /*
            同理，conn为true情况，close假如没有，事务也会提交
            假如conn为false，并且已经主动调用commit了，这里再调用sqlsession的close,并不会将事务回滚，因为事务已经提交了
         */
        sqlSession.close();
        return ResultVo.valueOfSuccess();
    }


    @RequestMapping("/testMoreSqlsession")
    public ResultVo testMoreSqlsession(){

        Student student = new Student();
        student.setId(4L);
        student.setStudentName("123456" + System.currentTimeMillis());

        /**
         * 同一个mapper层执行两次，sqlsession会是相同的吗？
         */
        int res = studentMapper.update(student);
        res = studentMapper.update(student);

        User user = new User();
        user.setFirstname("333");
        user.setLastname("444");
        res = userMapper.insert(user);

        return ResultVo.valueOfSuccess();
    }

    /**
     * 验证 时间戳精确到什么级别？
     * @return
     */
    @RequestMapping("/testTime")
    public String testTime(@RequestParam String firstname){
        User userList = userMapper.getUserByFirstname(firstname);
        String str = DateUtils.toStr(userList.getCreateTime(), PATTERN_yyyy_MM_dd_HH_mm_ss_SSS);
        return JacksonUtils.toJSONString(str);
    }
}
