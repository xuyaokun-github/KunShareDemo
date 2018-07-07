package cn.com.kun.foo.threadservice;

import cn.com.kun.common.vo.ResultVo;

public class ThreadServiceOne {

    public ResultVo getResult(){

        try {
//            Thread.sleep(2000);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResultVo.valueOfSuccess("执行成功");
    }


}
