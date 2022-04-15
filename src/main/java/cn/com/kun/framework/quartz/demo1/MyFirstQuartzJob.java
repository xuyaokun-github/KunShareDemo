//package cn.com.kun.framework.quartz.demo1;
//
//import cn.com.kun.framework.quartz.service.MyQuartzDemoService;
//import org.quartz.JobExecutionContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
///**
// * QuartzJobBean的子类，就是具体要执行的任务
// *
// * Created by xuyaokun On 2020/6/2 23:08
// * @desc:
// */
//public class MyFirstQuartzJob extends QuartzJobBean {
//
//    /**
//     * QuartzJobBean它最终会定义成spring的bean，所以在这里用注入，它也是能识别的。
//     */
//    @Autowired
//    MyQuartzDemoService myQuartzDemoService;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) {
//        myQuartzDemoService.method();
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                + " 执行MyFirstQuartzJob，线程名：" + Thread.currentThread().getName());
//        System.out.println("Hi! :" + jobExecutionContext.getJobDetail().getKey());
//    }
//}
