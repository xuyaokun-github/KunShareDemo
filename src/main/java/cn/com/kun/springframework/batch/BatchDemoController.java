package cn.com.kun.springframework.batch;

import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.springframework.batch.common.BatchJobOperator;
import cn.com.kun.springframework.batch.common.BatchRateLimitDynamicCheckScheduler;
import cn.com.kun.springframework.batch.common.BatchRateLimiterHolder;
import cn.com.kun.springframework.batch.common.SimpleStopHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 批处理测试控制器
 * author:xuyaokun_kzx
 * date:2021/5/20
 * desc:
*/
@RestController
public class BatchDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BatchDemoController.class);


    @Autowired
    BatchJobOperator batchJobOperator;

    @Autowired
    @Qualifier("myFirstJob")
    Job myFirstJob;

    @Autowired
    @Qualifier("mySecondJob")
    Job mySecondJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobLauncher customJobLauncher;

    @Autowired
    @Qualifier("myJob3")
    Job demoJob3;

    @Autowired
    @Qualifier("myJob5")
    Job myJob5;

    @Autowired
    BatchRateLimitDynamicCheckScheduler batchRateLimitDynamicCheckScheduler;

    /**
     * 测试job1
     * @return
     */
    @GetMapping("/testBatchJob1")
    public String testBatchJob1() throws Exception {

        SimpleStopHelper.removeStopFlag("myFirstJob");
        /*
            可以用手动的方式，触发Job运行
         */
        String jobId = UUID.randomUUID().toString();
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("jobName", "myFirstJob")
                .addString("sourceFilePath", "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file.txt")
                .addString("jobId", jobId)
                .toJobParameters();

        //注册限流器
        BatchRateLimiterHolder.registerRateLimiter(jobId, 100);

        LOGGER.info("启动myFirstJob");
        JobExecution execution = jobLauncher.run(myFirstJob, jobParameters);
        System.out.println(execution.toString());

        return "success";
    }

    /**
     * 测试job1
     * @return
     */
    @GetMapping("/testBatchJob1ByAsync")
    public String testBatchJob1ByAsync() throws Exception {

        new Thread(()->{

            while (true){
                ThreadUtils.sleep(15*1000);
                try {
                    testBatchJob1();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return "success";
    }

    /**
     * 测试job1（同时启动多个）
     * @return
     */
    @GetMapping("/testBatchJob1-more")
    public String testBatchJob1More() throws Exception {

        SimpleStopHelper.removeStopFlag("myFirstJob");

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                //组织自定义参数，参数可以给读写操作去使用
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .addString("jobName", "myFirstJob")
                        .addString("sourceFilePath", "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file-" + finalI + ".txt")
                        .toJobParameters();
                JobExecution execution = null;

                while (true){
                    try {
                        //customJobLauncher
                        execution = jobLauncher.run(myFirstJob, jobParameters);
                        //可以使用自定义的JobLauncher控制同时执行任务数。假如到达了上限，会返回一个Fail的execution，而不会抛异常
//                        execution = customJobLauncher.run(myFirstJob, jobParameters);
                        if (execution != null){
                            if (execution.getStatus().equals(BatchStatus.FAILED)){
                                LOGGER.info("任务执行失败");
                            }else {
                                //假如用了线程池，任务执行成功，返回的状态是 BatchStatus.STARTING
                                //假如没用线程池，执行成功，返回的状态是COMPLETED，因为是单线程执行，即本线程完成执行
                                LOGGER.info("任务执行结果：{}", execution.toString());
                            }
                        }
                        break;
                    } catch (Exception e) {
                        //这里是否能识别到死锁异常?
                        /*
                            org.springframework.dao.DeadlockLoserDataAccessException: PreparedStatementCallback; SQL [INSERT into BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, JOB_NAME, JOB_KEY, VERSION) values (?, ?, ?, ?)]; Deadlock found when trying to get lock; try restarting transaction; nested exception is com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException: Deadlock found when trying to get lock; try restarting transaction
                        */
                        if(e instanceof DeadlockLoserDataAccessException){
                            //
                            if(e.getMessage().contains("Deadlock found when trying to get lock")){
                                LOGGER.info("任务[{}]出现死锁异常,准备重试!", "myFirstJob");
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                continue;
                            }
                        }else{
                            LOGGER.error("任务执行异常", e);
                        }
                        break;
                    }
                }

            }, "BatchJob1-Thread-" + finalI).start();
        }


        return "success";
    }

    @GetMapping("/testStopBatchJob1")
    public String testStopBatchJob1() throws Exception{

        SimpleStopHelper.markStop("myFirstJob");
        return "success";
    }

    @GetMapping("/testRemoveBatchJob1StopFlag")
    public String testRemoveBatchJob1StopFlag() throws Exception{

        SimpleStopHelper.removeStopFlag("myFirstJob");
        return "success";
    }

    @GetMapping("/testBatchJob2")
    public String testBatchJob2() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
        可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(mySecondJob, jobParameters);
        System.out.println(execution.toString());

        //输出具体的错误信息
        List<Throwable> throwables = execution.getFailureExceptions();
        if (throwables != null && !throwables.isEmpty()){
            for (Throwable throwable : throwables){
                LOGGER.info("Job执行过程出现的异常", throwable);
            }
        }
        return "success";
    }


    @GetMapping("/testBatch3")
    public String testBatch3() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
        可以用手动的方式，触发Job运行
         */
        
        //启动两个线程去跑job
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                //组织自定义参数，参数可以给读写操作去使用
                JobParameters jobParameters = new JobParametersBuilder().addLong("runTime", System.currentTimeMillis())
                        .toJobParameters();
                JobExecution execution = null;
                try {
                    execution = jobLauncher.run(demoJob3, jobParameters);
                } catch (JobExecutionAlreadyRunningException e) {
                    e.printStackTrace();
                } catch (JobRestartException e) {
                    e.printStackTrace();
                } catch (JobInstanceAlreadyCompleteException e) {
                    e.printStackTrace();
                } catch (JobParametersInvalidException e) {
                    e.printStackTrace();
                }
                System.out.println(execution.toString());
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return "success";
    }





    /**
     * 可重复执行
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/testBatchJob4")
    public String testBatchJob4() throws Exception {

        /*
           可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("firstname", "345")
                .addString("date", "" + System.currentTimeMillis()) //可以添加多个参数
                .toJobParameters();
        //获取Job,job是单例，step的作用域是Step
        Job job = SpringContextUtil.getBean("myJob4");
        JobExecution execution = jobLauncher.run(job, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }

    @GetMapping("/testBatchJob5")
    public String testBatchJob5() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
            可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addString("firstname", "fisrt42")
                .addString("date", "" + System.currentTimeMillis()) //可以添加多个参数
                .toJobParameters();
        JobExecution execution = jobLauncher.run(myJob5, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }

    @GetMapping("/testRestartJob")
    public String testRestartJob() throws Exception {


//        Set<JobExecution> executionSet = jobExplorer.findRunningJobExecutions("myFirstJob");

        //job instanceId
//        jobExplorer.getJobInstance((long) 888);

        //执行ID
//        jobOperator.restart(999);
        return "success";
    }

    @GetMapping("/testRestartJobByExecutionId")
    public String testRestartJobByExecutionId(@RequestParam long executionId) throws Exception {

        //续跑前，先清除停止标记
        SimpleStopHelper.removeStopFlag("myFirstJob");
        batchJobOperator.restart(executionId);

        return "success";
    }


}
