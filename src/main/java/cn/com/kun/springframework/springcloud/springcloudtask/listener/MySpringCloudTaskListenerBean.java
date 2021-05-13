package cn.com.kun.springframework.springcloud.springcloudtask.listener;

import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Component
public class MySpringCloudTaskListenerBean {

    @BeforeTask
    public void methodA(TaskExecution taskExecution) {
        System.out.println("cn.com.kun.springframework.springcloud.springcloudtask.listener.MySpringCloudTaskListenerBean.methodA");
    }

    @AfterTask
    public void methodB(TaskExecution taskExecution) {
        System.out.println("cn.com.kun.springframework.springcloud.springcloudtask.listener.MySpringCloudTaskListenerBean.methodB");
    }

    @FailedTask
    public void methodC(TaskExecution taskExecution, Throwable throwable) {
        System.out.println("cn.com.kun.springframework.springcloud.springcloudtask.listener.MySpringCloudTaskListenerBean.methodC");
    }
}
