package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.springframework.batch.common.SimpleStopHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;

public class UserFileItemReader extends FlatFileItemReader<UserFileItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFileItemItemProcessor.class);

    private String jobName;


    @Override
    public UserFileItem read() throws Exception, UnexpectedInputException, ParseException {

        if (SimpleStopHelper.isNeedStop(jobName)){
            LOGGER.info("识别到停止标志，主动结束Job[{}]", jobName);
//            LOGGER.info("当前process阶段userFileItem：{}", userFileItem.getUid());
            throw new RuntimeException("Job主动停止");
        }
        return super.read();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
