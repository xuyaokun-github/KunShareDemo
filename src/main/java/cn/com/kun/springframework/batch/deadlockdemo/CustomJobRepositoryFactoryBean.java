package cn.com.kun.springframework.batch.deadlockdemo;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

/**
 * 走JDK代理
 *
 * author:xuyaokun_kzx
 * date:2022/6/22
 * desc:
*/
public class CustomJobRepositoryFactoryBean implements FactoryBean<ICustomJobRepository>, InitializingBean {

    private ProxyFactory proxyFactory;

    private DataSource dataSource;

    private PlatformTransactionManager transactionManager;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //
        transactionManager = buildTransactionManager();
        initializeProxy();
    }

    protected PlatformTransactionManager createTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource);
    }

    private PlatformTransactionManager buildTransactionManager() {
        PlatformTransactionManager transactionManager = createTransactionManager();
        return transactionManager;
    }

    private void initializeProxy() throws Exception {
        if (proxyFactory == null) {
            proxyFactory = new ProxyFactory();
            TransactionInterceptor advice = new TransactionInterceptor(transactionManager,
                    PropertiesConverter.stringToProperties("insert*=PROPAGATION_REQUIRES_NEW,"
                            + "ISOLATION_SERIALIZABLE" + "\ngetLastJobExecution*=PROPAGATION_REQUIRES_NEW,"
                            + "ISOLATION_SERIALIZABLE" + "\n*=PROPAGATION_REQUIRED"));
            if (true) {
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        if (TransactionSynchronizationManager.isActualTransactionActive()) {
                            throw new IllegalStateException(
                                    "Existing transaction detected in JobRepository. "
                                            + "Please fix this and try again (e.g. remove @Transactional annotations from client).");
                        }
                        return invocation.proceed();
                    }
                });
                NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
                pointcut.addMethodName("insert*");
                advisor.setPointcut(pointcut);
                proxyFactory.addAdvisor(advisor);
            }
            proxyFactory.addAdvice(advice);
            proxyFactory.setProxyTargetClass(false);
            proxyFactory.addInterface(ICustomJobRepository.class);
            proxyFactory.setTarget(getTarget());
        }
    }

    @Override
    public ICustomJobRepository getObject() throws Exception {

        return (ICustomJobRepository) proxyFactory.getProxy(getClass().getClassLoader());
    }


    private Object getTarget() throws Exception {
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

        //模仿Batch框架实现一个序列号增长器
        DataFieldMaxValueIncrementerFactory incrementerFactory = new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
        DataFieldMaxValueIncrementer incrementer = incrementerFactory.getIncrementer("MYSQL",  "BATCH_JOB_SEQ");
        return new CustomJobRepository(jdbcOperations, incrementer);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return ICustomJobRepository.class;
    }


}
