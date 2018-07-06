package cn.com.kun.foo;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.foo.threadservice.ThreadServiceOne;
import cn.com.kun.thread.SimpleThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TestHello {

    public static void main(String[] args) {

        ResultVo resultVo = ResultVo.valueOfSuccess(1);


        List<Callable<ResultVo>> tasks = new ArrayList<Callable<ResultVo>>();
        //添加到线程池
        tasks.add(new Callable<ResultVo>() {
            public ResultVo call() throws Exception {
                return getResult();
            }
        });
        System.out.println("开始执行多线程任务");
        List<ResultVo> taskResults = SimpleThreadUtil.executeWithException(tasks);
        taskResults.stream().forEach(u -> System.out.println(u.getValue()));
    }

    public static ResultVo getResult(){
        ThreadServiceOne serviceOne = new ThreadServiceOne();
        ResultVo resultVo = serviceOne.getResult();
        return resultVo;
    }

}
