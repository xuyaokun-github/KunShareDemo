package cn.com.kun.framework.netty.hashedWheelTimer;

import cn.com.kun.common.utils.DateUtils;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * 每隔3秒，针对锁进行续约
 * 参考Redisson的实现
 *
 * author:xuyaokun_kzx
 * date:2021/11/30
 * desc:
*/
public class HashedWheelTimerDemo2 {

    static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS);


    public static void main(String[] args) {

        System.out.println("start :" + DateUtils.now());
        hashedWheelTimer.newTimeout(new MyTimerTask(), 3, TimeUnit.SECONDS);
        System.out.println("投放任务完毕");

    }

    static class MyTimerTask implements TimerTask{

        @Override
        public void run(Timeout timeout) throws Exception {
            System.out.println("续约锁 :" + DateUtils.now());
            if (true){
                hashedWheelTimer.newTimeout(new MyTimerTask(), 3, TimeUnit.SECONDS);
            }
        }
    }

}