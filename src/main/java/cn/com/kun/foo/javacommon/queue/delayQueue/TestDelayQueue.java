package cn.com.kun.foo.javacommon.queue.delayQueue;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;

import java.util.concurrent.DelayQueue;

/**
 * 延迟队列
 * 特点：时间到了才能将元素取出
 *
 * author:xuyaokun_kzx
 * date:2021/11/30
 * desc:
*/
public class TestDelayQueue {

    public static void main(String[] args) throws InterruptedException {

        DelayQueue<DelayedTask> queue = new DelayQueue<>();

        DelayedTask delayedTask = new DelayedTask("taskOne", 5);
//        queue.offer(delayedTask, 10, TimeUnit.SECONDS);
        //投放一个任务，5秒后执行的
        queue.offer(delayedTask, 0, null);
        //投放一个任务，30秒后执行的
        queue.offer(new DelayedTask("taskOne2", 30));

        //然后任务执行线程一直轮询队列，假如有就执行
        while (true){
            DelayedTask target = queue.take();
            if (target != null){
                //能取得到，说明是可以跑的，已经到时间了
                System.out.println("获取到元素，当前时间：" + DateUtils.now());
                System.out.println(JacksonUtils.toJSONString(target));
            }
        }


    }


}
