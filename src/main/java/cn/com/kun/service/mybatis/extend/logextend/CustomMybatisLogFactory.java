package cn.com.kun.service.mybatis.extend.logextend;

import org.apache.ibatis.logging.LogFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMybatisLogFactory {

    public static final CustomMybatisLogFactory instance = new CustomMybatisLogFactory();

    static {
        LogFactory.useCustomLogging(CustomMybatisSlf4jImpl.class);
    }


}
