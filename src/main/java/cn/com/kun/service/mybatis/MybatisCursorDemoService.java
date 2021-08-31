package cn.com.kun.service.mybatis;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.mapper.UserMapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MybatisCursorDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MybatisCursorDemoService.class);

//    @Autowired
//    UserMapper userMapper;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    /**
     * 测试流式查询
     * @return
     */
    public ResultVo test1(){
        try (
                // 使用 sqlSession 手动获取 Mapper 对象，否则会出现 "A Cursor is already closed" 异常
                SqlSession sqlSession = sqlSessionFactory.openSession();
                Cursor<User> cursor = sqlSession.getMapper(UserMapper.class).findAllStream()
        ) {
            cursor.forEach(user -> {
//                System.out.println(user);
                LOGGER.info("user info:{}", JacksonUtils.toJSONString(user));
            });
            //索引是从0开始！
            LOGGER.info("流式查询全部结束，处理总数：{}", cursor.getCurrentIndex() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultVo.valueOfSuccess("SUCCESS");
    }

}
