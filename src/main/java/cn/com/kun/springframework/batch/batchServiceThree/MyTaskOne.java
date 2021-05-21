package cn.com.kun.springframework.batch.batchServiceThree;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class MyTaskOne implements Tasklet {

    private String content;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        System.out.println("MyTaskOne start..");
        System.out.println("MyTaskOne " + Thread.currentThread().getName() + " " + content);
        content = "kunghsu content!!" + Thread.currentThread().getName();//赋值
        System.out.println("MyTaskOne done..");

        if (false){
            //模拟处理失败,然后想重试，就return CONTINUABLE
            //假如判断是CONTINUABLE，会再一次重跑这个Tasklet，一直继续直至返回FINISHED
            return RepeatStatus.CONTINUABLE;
        }


        return RepeatStatus.FINISHED;
    }
}
