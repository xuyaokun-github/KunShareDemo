package cn.com.kun.controller.jdbc;

import cn.com.kun.bean.entity.StudentDO;
import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;
import cn.com.kun.component.jdbc.CommonJdbcStore;
import cn.com.kun.component.jdbc.PreparedStatementParamProvider;
import cn.com.kun.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    CommonJdbcStore commonJdbcStore;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/test1")
    public String test1(){

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
//        int res = commonJdbcStore.update(sql);//mysql会自动替换特殊字符（反例）
        //正例
        int res = commonJdbcStore.update(sql2, new PreparedStatementParamProvider() {
            @Override
            public void initPreparedStatementParam(PreparedStatement ps) {
                try {
                    ps.setString(1, jsonString);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        return "kunghsu";
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


    @GetMapping("/testSelectList")
    public String testSelectList() throws SQLException {

        String sql = "select id as id,id_card as idCard,student_name as studentName,address as address,create_time as createTime from tbl_student " +
//                "where student_name='string11' " +
//                " limit 1";
                  " ";

        List<StudentDO> studentDO = commonJdbcStore.selectList(sql, StudentDO.class);
        LOGGER.info("{}", JacksonUtils.toJSONString(studentDO));
        return "kunghsu";
    }
}
