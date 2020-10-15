package cn.com.kun.controller.batch;

import cn.com.kun.utils.SpringContextUtil;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchDemoController {

    @Autowired
    @Qualifier("myFirstJob")
    Job myFirstJob;

    @Autowired
    JobLauncher jobLauncher;

    @RequestMapping("/testBatch")
    public String testBatch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
        可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(myFirstJob, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }


    @Autowired
    @Qualifier("mySecondJob")
    Job mySecondJob;

    @RequestMapping("/testBatch2")
    public String testBatch2() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
        可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(mySecondJob, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }

    @Autowired
    @Qualifier("demoJob3")
    Job demoJob3;


    @RequestMapping("/testBatch3")
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

    @Autowired
    @Qualifier("myJob3")
    Job myJob3;

    @RequestMapping("/testBatch33")
    public String testBatch33() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        /*
            可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addString("firstname", "fisrt42")
                .addString("date", "" + System.currentTimeMillis()) //可以添加多个参数
                .toJobParameters();
        JobExecution execution = jobLauncher.run(myJob3, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }


    /**
     * 可重复执行
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/testBatchJob4")
    public String testBatchJob4() throws Exception {

        /*
           可以用手动的方式，触发Job运行
         */
        //组织自定义参数，参数可以给读写操作去使用
        JobParameters jobParameters = new JobParametersBuilder().addString("firstname", "345")
                .addString("date", "" + System.currentTimeMillis()) //可以添加多个参数
                .toJobParameters();
        //获取Job,job是单例，step的作用域是Step
        Job job = SpringContextUtil.getBean("myBatchJob4");
        JobExecution execution = jobLauncher.run(job, jobParameters);
        System.out.println(execution.toString());
        return "success";
    }



}
