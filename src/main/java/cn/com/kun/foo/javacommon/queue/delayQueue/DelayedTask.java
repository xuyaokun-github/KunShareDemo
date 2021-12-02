package cn.com.kun.foo.javacommon.queue.delayQueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedTask implements Delayed {

    private String taskName;

    private long timeoutSecond;

    private long removeTime;

    public DelayedTask(String taskName, Integer seconds) {
        this.taskName = taskName;
        //设置过期的秒数
        this.timeoutSecond = System.currentTimeMillis() / 1000 + seconds;
//        this.removeTime = TimeUnit.NANOSECONDS.convert(liveTime, TimeUnit.NANOSECONDS) + System.nanoTime();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public long getDelay(TimeUnit unit) {

        //虽然传入的是TimeUnit.NANOSECONDS，但可以不用

        //这个方法，只要返回值比0大，该元素就未过期
        //假设将会在第1000秒时过期，目前还只是998秒，那相减之后还是大于0，所以该任务仍未过期
        return timeoutSecond - (System.currentTimeMillis() / 1000);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
