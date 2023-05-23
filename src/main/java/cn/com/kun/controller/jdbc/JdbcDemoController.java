package cn.com.kun.controller.jdbc;

import cn.com.kun.bean.entity.StudentDO;
import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;
import cn.com.kun.component.jdbc.CommonDbUtilsJdbcStore;
import cn.com.kun.component.jdbc.CommonJdbcStore;
import cn.com.kun.component.jdbc.PreparedStatementParamProvider;
import cn.com.kun.mapper.StudentMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/jdbc-demo")
@RestController
public class JdbcDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonJdbcStore.class);

    @Autowired
    private CommonJdbcStore commonJdbcStore;

    @Autowired
    private CommonDbUtilsJdbcStore commonDbUtilsJdbcStore;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired(required = false)
    private DataSource dataSource;

    @GetMapping("/testSelectBean")
    public String testSelectBean(){

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime " +
                "from tbl_student " +
                "WHERE id = 8 " +
                "limit 1";
        StudentDO studentDO = commonJdbcStore.select(sql, StudentDO.class);

        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        LOGGER.info("查询得到的jsonString: {}", studentDO.getAddress());

//        Student student2 = studentMapper.getStudentById(8L);
//        LOGGER.info("查询得到的jsonString(mybatis): {}", student2.getAddress());



        return "kunghsu";
    }


    @GetMapping("/testSelectBeanByCommonDbUtilsJdbcStore")
    public String testSelectBeanByCommonDbUtilsJdbcStore(){

        String sql2 = "select id ,id_card ,student_name ,address ,create_time " +
                "from tbl_student " +
                "WHERE id = 8 " +
                "limit 1";
        StudentDO studentDO = commonDbUtilsJdbcStore.select(sql2, StudentDO.class);

        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        LOGGER.info("查询得到的jsonString: {}", studentDO.getAddress());

        return "kunghsu";
    }

    @GetMapping("/testSelectBeanByCommonDbUtilsJdbcStoreByAsyncThread")
    public String testSelectBeanByCommonDbUtilsJdbcStoreByAsyncThread(){

        new Thread(()->{
            while (true){
                String sql2 = "select id ,id_card ,student_name ,address ,create_time " +
                        "from tbl_student " +
                        "WHERE id = 8 " +
                        "limit 1";
                 StudentDO studentDO = commonDbUtilsJdbcStore.select(sql2, StudentDO.class);
                ThreadUtils.sleep(300);
            }
        }).start();
        return "kunghsu";
    }


    @GetMapping("/testUpdateStudent")
    public String testUpdateStudent(){

        User user = new User();
        user.setFirstname("aaaaa");
        user.setLastname("bbbbbb");
        Map<String, String> param = new HashMap<>();
        param.put("requestContent", JacksonUtils.toJSONString(user));
        param.put("time", "" + System.currentTimeMillis());

        String sql = "update tbl_student set address='%s' " +
                "WHERE id = 8";
        String sql2 = "update tbl_student set address=? " +
                "WHERE id = 8";
        String jsonString = JacksonUtils.toJSONString(param);
        LOGGER.info("写DB的jsonString: {}", jsonString);
        sql = String.format(sql, jsonString);
        //反例
//        int res = commonJdbcStore.update(sql);//mysql会自动替换特殊字符（反例）
        //正例
//        int res = commonJdbcStore.update(sql2, new PreparedStatementParamProvider() {
//            @Override
//            public void initPreparedStatementParam(PreparedStatement ps) {
//                try {
//                    ps.setString(1, jsonString);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        int res = commonDbUtilsJdbcStore.update(sql2, jsonString);
        return "kunghsu";
    }

    @GetMapping("/testUpdateStudentByCommonDbUtilsJdbcStore")
    public ResultVo testUpdateStudentByCommonDbUtilsJdbcStore(){

        String sql1 = "update tbl_student set address='" + System.currentTimeMillis() + "'" +
                "WHERE id = 8";
        String sql2 = "update tbl_student set address=? " +
                "WHERE id = 8";
        int res = commonDbUtilsJdbcStore.update(sql1);

        return ResultVo.valueOfSuccess(res);
    }

    @GetMapping("/test2")
    public String test2(){

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime from tbl_student " +
                "where student_name='string11' " +
                " limit 1";
        StudentDO studentDO = commonJdbcStore.select(sql, StudentDO.class);
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        return "kunghsu";
    }

    @GetMapping("/test3")
    public String test3(){

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime from tbl_student " +
                "where student_name = ? " +
                " limit 1";
        StudentDO studentDO = commonJdbcStore.select(sql, StudentDO.class, new PreparedStatementParamProvider() {
            @Override
            public void initPreparedStatementParam(PreparedStatement ps) {
                try {
                    ps.setString(1, "string11");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        studentDO = commonJdbcStore.select(sql, StudentDO.class, new PreparedStatementParamProvider() {
            @Override
            public void initPreparedStatementParam(PreparedStatement ps) {
                try {
                    ps.setString(1, "tmac");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        return "kunghsu";
    }

    @GetMapping("/testSelectPreparedStatementByCommonDbUtilsJdbcStore")
    public String testSelectPreparedStatementByCommonDbUtilsJdbcStore(){

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime from tbl_student " +
                "where student_name = ? " +
                " limit 1";

        StudentDO studentDO = commonDbUtilsJdbcStore.select(sql, StudentDO.class, "string11");
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        studentDO = commonDbUtilsJdbcStore.select(sql, StudentDO.class, "tmac1");
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        return "kunghsu";
    }

    /**
     * datetime类型，实体类中用java.util.Date
     * @return
     */
    @GetMapping("/test4")
    public String test4(){

        String sql = "select id as id,resource as resource,description as description,request_id as requestTd,request_time as requestTime from tbl_database_lock " +
                "where id=1 " +
                " limit 1";
        DbLockDO dbLockDO = commonJdbcStore.select(sql, DbLockDO.class);
        LOGGER.info("{}", JacksonUtils.toJSONString(dbLockDO));
        return "kunghsu";
    }

    @GetMapping("/testTImeZone")
    public String testTImeZone(){

        Date date = new java.util.Date();
        String sql = "update tbl_database_lock set request_time='%s' " +
                "WHERE id = 3";
        sql = String.format(sql, DateUtils.toStr(date, "yyyy-MM-dd HH:mm:ss"));
        int res = commonJdbcStore.update(sql);
        sql = "select id as id,resource as resource,description as description,request_id as requestTd,request_time as requestTime from tbl_database_lock " +
                "where id=3 " +
                " limit 1";
        DbLockDO dbLockDO = commonJdbcStore.select(sql, DbLockDO.class);
        Date afterQueryTime = dbLockDO.getRequestTime();
        LOGGER.info("{}", JacksonUtils.toJSONString(dbLockDO));
        LOGGER.info("相减后:{}", (afterQueryTime.getTime() - date.getTime()));
        return "kunghsu";
    }


    @GetMapping("/testSelectListByCommonDbUtilsJdbcStore")
    public String testSelectListByCommonDbUtilsJdbcStore() throws SQLException {

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime from tbl_student " +
//                "where student_name='string11' " +
//                " limit 1";
                  " ";
        List<StudentDO> studentDO = commonDbUtilsJdbcStore.selectList(sql, StudentDO.class);
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        return "kunghsu";
    }


    @GetMapping("/testSelectListBy")
    public String testSelectList() throws SQLException {

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime from tbl_student " +
//                "where student_name='string11' " +
//                " limit 1";
                " ";

        List<StudentDO> studentDO = commonJdbcStore.selectList(sql, StudentDO.class);
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        return "kunghsu";
    }


    @GetMapping("/testJDBCSaveBatch")
    public String testJDBCSaveBatch() throws SQLException {


        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into open_test(a,b,c,d,e,f,g,h,i,j,k) values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (int i = 0; i < 1000; i++) {
                statement.setString(1,"a" + i);
                statement.setString(2,"b" + i);
                statement.setString(3, "c" + i);
                statement.setString(4,"d" + i);
                statement.setString(5,"e" + i);
                statement.setString(6,"f" + i);
                statement.setString(7,"g" + i);
                statement.setString(8,"h" + i);
                statement.setString(9,"i" + i);
                statement.setString(10,"j" + i);
                statement.setString(11,"k" + i);
                statement.addBatch();
            }
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("JDBC save batch");
            statement.executeBatch();
            connection.commit();
            stopWatch.stop();
            LOGGER.info("JDBC save batch：" + stopWatch.getTotalTimeMillis());
        } finally {
            statement.close();
            sqlSession.close();
        }

        return "kunghsu";
    }
}

