package cn.com.kun.service.mybatis.interceptor;

import cn.com.kun.common.utils.JacksonUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

/**
 * 拦截增删查改，输出具体sql与耗时
 * Created by xuyaokun On 2021/11/2 20:42
 *
 * @desc:
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Component
public class MysqlSqlLogInterceptor implements Interceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MysqlSqlLogInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object target = invocation.getTarget();

        long startTime = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) target;
        try {
            return invocation.proceed();
        } finally {
            long sqlCost = System.currentTimeMillis() - startTime;
            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            sql = simpleFormat(sql);
            Object parameterObject = boundSql.getParameterObject();
            //生产建议输出info级别，方便查看执行SQL
            LOGGER.debug("Execute done, sql:{} params:{} cost:{}", sql, JacksonUtils.toJSONString(parameterObject), sqlCost);
        }
    }

    private String simpleFormat(String sql) {
        sql = sql.replaceAll("[\\s\n ]+"," ");
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
