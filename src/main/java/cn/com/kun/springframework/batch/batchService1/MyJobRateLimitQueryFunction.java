package cn.com.kun.springframework.batch.batchService1;

import cn.com.kun.springframework.batch.common.JobRateLimitQueryFunction;
import org.springframework.stereotype.Component;

@Component
public class MyJobRateLimitQueryFunction implements JobRateLimitQueryFunction {

    //这里注入具体的dao层等

    @Override
    public double queryRateLimit(String jobId) {

        return 1;
    }

}
