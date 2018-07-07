package cn.com.kun.foo;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.foo.threadservice.ThreadServiceOne;
import cn.com.kun.thread.SimpleThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TestHello {

    public static void main(String[] args) {

        /**
         * 这个数设置为2000 已经很容易看出快慢差距
         *（实践验证发现 共用一个池 更快，而且数字越大，越明显，设置为5000就已经看出差距很大了）
         */
        int threadNum = 2;//并发的线程任务数量（每次放多少线程到线程池中）


        int mainThreadNum = 5000;//模拟五个用户发起了查待办请求

        long start = System.currentTimeMillis();

        for (int i = 0; i < mainThreadNum; i++) {
            Thread thread = new Thread(() -> {
                List<Callable<ResultVo>> tasks = new ArrayList<Callable<ResultVo>>();
                for (int j = 0; j < threadNum; j++) {
                    //添加到线程池
                    tasks.add(new Callable<ResultVo>() {
                        public ResultVo call() throws Exception {
                            //模拟具体的查待办任务的业务层方法
                            return getResult();
                        }
                    });
                }

                System.out.println("开始执行多线程任务");
                //每个线程进来都会new一个新线程池
//                List<ResultVo> taskResults = SimpleThreadUtil.executeWithException(tasks);
                //所有线程公用一个线程池
                List<ResultVo> taskResults = SimpleThreadUtil.executeIgnoreException(tasks);

                System.out.println("当前线程执行完毕：" + Thread.currentThread().getName());
                System.out.println("耗时：" + (System.currentTimeMillis() - start));
            });
            thread.start();
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }


//        System.out.println("耗时：" + (System.currentTimeMillis() - start));

//        taskResults.stream().forEach(u -> System.out.println(u.getValue()));



    }

    public static ResultVo getResult(){
        ThreadServiceOne serviceOne = new ThreadServiceOne();
        ResultVo resultVo = serviceOne.getResult();
        return resultVo;
    }

}
