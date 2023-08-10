package cn.com.kun.service.mybatis.interceptor;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 锁异常重试拦截器
 *
 * author:xuyaokun_kzx
 * date:2023/7/18
 * desc:
*/
@Intercepts({@Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
@Component
public class LockExceptionRetryInterceptor implements Interceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(LockExceptionRetryInterceptor.class);

    /**
     * 填要拦截的dao层方法对应的MapperId
     */
    private Set<String> targetMapperIdSet = new HashSet<>();

    /**
     * 填小写表名
     */
    private Set<String> targetTableNameSet = new HashSet<>();

    @PostConstruct
    public void init(){
        //需要拦截的dao层方法
        targetMapperIdSet.add("cn.com.kun.mapper.StudentMapper.insert");
        targetTableNameSet.add("tbl_student");
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement ms = null;
        Object target = invocation.getTarget();
        StatementHandler statementHandler = (StatementHandler) target;
        BoundSql boundSql = statementHandler.getBoundSql();
        if (isTableNameMatch(boundSql)){

            if (target instanceof RoutingStatementHandler){
                RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) invocation.getTarget();
                MappedStatement mappedStatement = getMappedStatement(routingStatementHandler);
                if (mappedStatement != null){
                    ms = mappedStatement;
                }
            }

            //这个方法获取不到MappedStatement（只能支持query类型方法）
//            Object[] args = invocation.getArgs();
//            if (args[0] instanceof DruidPooledPreparedStatement){
//                DruidPooledPreparedStatement druidPooledPreparedStatement = (DruidPooledPreparedStatement) args[0];
//                Statement statement = druidPooledPreparedStatement.getStatement();
//            }else if (args[0] instanceof MappedStatement) {
//                ms = (MappedStatement) args[0];
//            }

        }



        if (ms != null){
            if (targetMapperIdSet.contains(ms.getId())){
                while (true){
                    try {
                        return invocation.proceed();
                    }catch (Exception e){
                        if(isCannotAcquireLockException(e)){
                            LOGGER.error("插件捕获“无法获取锁异常”,准备重试");
                            continue;
                        } if (isDeadLockException(e)){
                            LOGGER.error("插件捕获“死锁异常”,准备重试");
                            continue;
                        }else {
                            throw e;
                        }
                    }
                }
            }else {
                return invocation.proceed();
            }
        }else {
            return invocation.proceed();
        }

    }

    private boolean isDeadLockException(Exception e) {

        if (e instanceof org.springframework.dao.DeadlockLoserDataAccessException){
            //这个还不一定能捕获到，插件里捕获到的是 java.lang.reflect.InvocationTargetException
            return true;
        }

        if (e instanceof java.lang.reflect.InvocationTargetException){
            InvocationTargetException targetException = (InvocationTargetException) e;
            Throwable target = targetException.getTargetException();
            if (target instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException && target.getMessage() != null &&
                    target.getMessage().contains("Deadlock found when trying to get lock")){
                return true;
            }
        }

        return false;
    }

    private boolean isTableNameMatch(BoundSql boundSql) {

        for (String tableName : targetTableNameSet){
            if (boundSql.getSql().contains(tableName) || boundSql.getSql().contains(tableName.toUpperCase())){
                return true;
            }
        }

        return false;
    }

    private MappedStatement getMappedStatement(RoutingStatementHandler routingStatementHandler) {

        MetaObject metaObject = MetaObject.forObject(routingStatementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        return mappedStatement;
    }


    /**
     * 获取锁失败异常
     *
     * @param e
     * @return
     */
    private boolean isCannotAcquireLockException(Exception e) {

        if (e instanceof org.springframework.dao.CannotAcquireLockException){
            return true;
        }

        if (e instanceof java.lang.reflect.InvocationTargetException){
            InvocationTargetException targetException = (InvocationTargetException) e;
            Throwable target = targetException.getTargetException();
            if (target instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException && target.getMessage() != null &&
                target.getMessage().contains("Lock wait timeout exceeded")){
                return true;
            }
        }

        return false;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}
