package cn.com.kun.framework.netty.hashedWheelTimer;

import cn.com.kun.common.utils.DateUtils;
import io.netty.util.HashedWheelTimer;

import java.util.concurrent.TimeUnit;

public class HashedWheelTimerDemo {

    public static void main(String[] args) {

        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);
        System.out.println("start :" + DateUtils.now());

        hashedWheelTimer.newTimeout(timeout -> {
            System.out.println("task :" + DateUtils.now());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1/0;
        }, 5, TimeUnit.SECONDS);

        hashedWheelTimer.newTimeout(timeout -> {
            System.out.println("task :" + DateUtils.now());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 1/0;
        }, 15, TimeUnit.SECONDS);

        System.out.println("投放任务完毕");

    }
}