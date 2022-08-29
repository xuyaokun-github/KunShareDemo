package cn.com.kun.springframework.batch.common;

public interface JobRateLimitQueryFunction {

    double queryRateLimit(String jobId);

}
