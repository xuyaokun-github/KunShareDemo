package cn.com.kun.springframework.batch.deadlockdemo;

import cn.com.kun.service.mybatis.impl.DeadLockDemoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import java.sql.Types;

public class CustomJobRepository implements ICustomJobRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeadLockDemoServiceImpl.class);

    private JdbcOperations jdbcOperations;

    private DataFieldMaxValueIncrementer incrementer;

    public CustomJobRepository(JdbcOperations jdbcOperations, DataFieldMaxValueIncrementer incrementer) {
        this.jdbcOperations = jdbcOperations;
        this.incrementer = incrementer;
    }

    public void show(){
        LOGGER.info("I am CustomJobRepository.");
    }

    public void insertInstance(Object[] parameters) {

        try {
            long id = incrementer.nextLongValue();
            parameters[0] = id;
            int res = jdbcOperations.update(
                    "INSERT into BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, JOB_NAME, JOB_KEY, VERSION) values (?, ?, ?, ?)",
                    parameters,
                    new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR,
                            Types.INTEGER });
            LOGGER.info("执行结果，影响条数：{}", res);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
