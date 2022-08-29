package cn.com.kun.springframework.batch.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 批处理任务操作服务层
 *
 * author:xuyaokun_kzx
 * date:2022/8/26
 * desc:
*/
@Component
public class BatchJobOperator {

    private final static Logger LOGGER = LoggerFactory.getLogger(BatchJobOperator.class);

    @Autowired
    private JobOperator jobOperator;

//    @Autowired
//    private JobExplorer jobExplorer;

    /**
     * 根据执行ID重启Job
     * @param executionId
     * @return
     */
    public boolean restart(long executionId) {

        //执行ID
        Long newExecutionId = null;
        try {
            newExecutionId = jobOperator.restart(executionId);
        } catch (Exception e) {
            LOGGER.error(String.format("batch job重启异常,执行ID:%s", executionId), e);
        }
        //假如能拿到新的执行ID,说明重启成功
        return newExecutionId != null && newExecutionId.longValue() > 0;
    }


}
