package cn.com.kun.springframework.springredis.taskassign;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务分配器
 * 知道有哪些任务可拉取，目前已经拉取了哪个任务
 *
 * author:xuyaokun_kzx
 * date:2022/5/5
 * desc:
*/
public class TaskAssignFetcher {

    /**
     * 可拉取的任务
     */
    private List<Object> taskList = new ArrayList<>();

    public void startKeepAliveThread(){


    }


}
