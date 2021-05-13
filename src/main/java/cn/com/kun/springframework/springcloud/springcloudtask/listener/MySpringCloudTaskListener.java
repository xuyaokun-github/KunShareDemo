package cn.com.kun.springframework.springcloud.springcloudtask.listener;

import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Component
public class MySpringCloudTaskListener implements TaskExecutionListener {

    @Override
    public void onTaskStartup(TaskExecution taskExecution) {
        System.out.println("cn.com.kun.springframework.springcloud.springcloudtask.listener.MySpringCloudTaskListener.onTaskStartup");
    }

    @Override
    public void onTaskEnd(TaskExecution taskExecution) {
        System.out.println("cn.com.kun.springframework.springcloud.springcloudtask.listener.MySpringCloudTaskListener.onTaskEnd");
    }

    @Override
    public void onTaskFailed(TaskExecution taskExecution, Throwable throwable) {
        System.out.println("cn.com.kun.springframework.springcloud.springcloudtask.listener.MySpringCloudTaskListener.onTaskFailed");
    }
}
