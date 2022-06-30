package cn.com.kun.springframework.batch;

import cn.com.kun.common.utils.SpringContextUtil;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Qualifier("myFirstJob")
    Job myFirstJob;

    @Autowired
    @Qualifier("mySecondJob")
    Job mySecondJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("myJob3")
    Job demoJob3;

    @Autowired
    @Qualifier("myJob5")
    Job myJob5;

    /**
     * 测试job1
     * @return
     */
    @GetMapping("/testBatchJob1")
    public String testBatch() throws Exception {

        SimpleStopHelper.removeStopFlag("importUserJob");
        /*
            可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .addString("jobName", "importUserJob")
                .addString("sourceFilePath", "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file.txt")
                .toJobParameters();
        JobExecution execution = jobLauncher.run(myFirstJob, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }

    /**
     * 测试job1（同时启动多个）
     * @return
     */
    @GetMapping("/testBatchJob1-more")
    public String testBatchJob1More() throws Exception {

        SimpleStopHelper.removeStopFlag("importUserJob");

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                //组织自定义参数，参数可以给读写操作去使用
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .addString("jobName", "importUserJob")
                        .addString("sourceFilePath", "D:\\home\\kunghsu\\big-file-test\\batchDemoOne-big-file-" + finalI + ".txt")
                        .toJobParameters();
                JobExecution execution = null;

                while (true){
                    try {
                        execution = jobLauncher.run(myFirstJob, jobParameters);
                        if (execution != null){
                            LOGGER.info("任务执行结果：{}", execution.toString());
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
                                LOGGER.info("任务[{}]出现死锁异常,准备重试!", "importUserJob");
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

        SimpleStopHelper.markStop("importUserJob");
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

}
