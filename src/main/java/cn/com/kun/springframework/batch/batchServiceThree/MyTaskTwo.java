package cn.com.kun.springframework.batch.batchServiceThree;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class MyTaskTwo implements Tasklet {

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("MyTaskTwo start..");

        //模拟一个延迟
        Thread.sleep(10000);

        Object obj = chunkContext.getAttribute("runTime");
        System.out.println(obj);
        System.out.println("MyTaskTwo done..");
        return RepeatStatus.FINISHED;
    }
}
