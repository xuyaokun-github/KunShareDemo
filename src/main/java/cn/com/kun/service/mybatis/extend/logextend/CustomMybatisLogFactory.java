package cn.com.kun.service.mybatis.extend.logextend;

import org.apache.ibatis.logging.LogFactory;

/**
 * 自定义的mybatis日志实现
 * 将默认的debug级别改成info级别
 *
 * author:xuyaokun_kzx
 * date:2021/11/3
 * desc:
*/
//加@Configuration的目的是让这个类被初始化，执行静态代码块
//@Configuration
public class CustomMybatisLogFactory {

    public static final CustomMybatisLogFactory instance = new CustomMybatisLogFactory();

    static {
        //参考自Slf4jImpl
        LogFactory.useCustomLogging(CustomMybatisSlf4jImpl.class);
    }


}
