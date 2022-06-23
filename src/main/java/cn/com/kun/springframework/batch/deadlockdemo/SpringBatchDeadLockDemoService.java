package cn.com.kun.springframework.batch.deadlockdemo;

import cn.com.kun.mapper.SpringBatchDeadLockDemoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Service
public class SpringBatchDeadLockDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringBatchDeadLockDemoService.class);

    @Autowired
    private SpringBatchDeadLockDemoMapper deadLockDemoMapper;

    JdbcOperations jdbcOperations;

    @Autowired
    DataSource dataSource;

    private DataFieldMaxValueIncrementerFactory incrementerFactory;

    DataFieldMaxValueIncrementer incrementer;


    @Autowired(required = false)
    private ICustomJobRepository customJobRepository;

    @PostConstruct
    public void init(){
        jdbcOperations = new JdbcTemplate(dataSource);

        //模仿Batch框架实现一个序列号增长器
        incrementerFactory = new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
        incrementer = incrementerFactory.getIncrementer("MYSQL",  "BATCH_JOB_SEQ");
    }

    public int selectMaxJobInstanceId() {
        int id = deadLockDemoMapper.selectMaxJobInstanceId();
        return id;
    }

    /**
     * 通过Mybatis方式并发插入，并没有出现问题(假如方法里只有插入，没有问题)
     *
     *
     *
     * @param map
     */
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.REPEATABLE_READ)
//    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE)
    public void insertBatchJobInstance(Map<String, Object> map) {
        deadLockDemoMapper.insertBatchJobInstance(map);
    }


    /**
     * 终于破案了！！！！！！！
     * Batch死锁的原因，就是因为同一个事务里首先做了查询，然后做了更新，就会导致
     * @param parameters
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation= Propagation.REQUIRES_NEW)
    public void insertBatchJobInstanceByJdbcTemplate(Object[] parameters) {

        try {
            RowMapper<JobInstance> rowMapper = new JobInstanceRowMapper();
            List<JobInstance> instances = jdbcOperations.query("SELECT JOB_INSTANCE_ID, JOB_NAME from BATCH_JOB_INSTANCE where JOB_NAME = ? and JOB_KEY = ?",
                    rowMapper, parameters[1], parameters[2]);
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

    private final class JobInstanceRowMapper implements RowMapper<JobInstance> {

        public JobInstanceRowMapper() {
        }

        @Override
        public JobInstance mapRow(ResultSet rs, int rowNum) throws SQLException {
            JobInstance jobInstance = new JobInstance(rs.getLong(1), rs.getString(2));
            // should always be at version=0 because they never get updated
            jobInstance.incrementVersion();
            return jobInstance;
        }
    }

    //改了事务管理器，没有复现死锁问题
//    @Transactional(isolation = Isolation.SERIALIZABLE, propagation= Propagation.REQUIRES_NEW, transactionManager = "deadlockDemoTransactionManager")
    public void insertBatchJobInstanceBySameTxManager(Object[] parameters) {

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

    public void insertBatchJobInstanceByProxy(Object[] parameters) {

        try {
            customJobRepository.insertInstance(parameters);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
