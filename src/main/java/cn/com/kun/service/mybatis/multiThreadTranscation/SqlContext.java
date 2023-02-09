package cn.com.kun.service.mybatis.multiThreadTranscation;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/** 获取sqlSession
 * @author 86182
 * @version V1.0
 */
@Component
public class SqlContext {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 可以直接通过SqlSessionFactory拿到SqlSession，不一定要通过SqlSessionTemplate
     * @return
     */
    public SqlSession getSqlSession(){
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        return sqlSessionFactory.openSession();
    }


}
