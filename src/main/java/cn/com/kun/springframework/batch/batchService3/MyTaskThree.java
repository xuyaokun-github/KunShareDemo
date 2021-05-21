package cn.com.kun.springframework.batch.batchService3;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map;

public class MyTaskThree implements Tasklet {

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        System.out.println("MyTaskThree start..");
        Map<String, Object> params = chunkContext.getStepContext().getJobParameters();
        System.out.println(params);
        System.out.println("MyTaskThree done..");

        //RepeatStatus有两种状态：FINISHED和CONTINUABLE
        return RepeatStatus.FINISHED;
    }
}
