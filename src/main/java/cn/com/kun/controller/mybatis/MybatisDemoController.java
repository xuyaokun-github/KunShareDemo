package cn.com.kun.controller.mybatis;

import cn.com.kun.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RequestMapping("/MybatisDemoController")
@RestController
public class MybatisDemoController {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    UserMapper userMapper;

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

        userMapper.query(null);
        return "kunghsu cn.com.kun.controller.mybatis.MybatisDemoController.test2";
    }


}
