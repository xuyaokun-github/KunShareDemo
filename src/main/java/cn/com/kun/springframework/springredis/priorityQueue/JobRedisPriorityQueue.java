package cn.com.kun.springframework.springredis.priorityQueue;

import cn.com.kun.springframework.springredis.vo.JobVO;
import org.springframework.stereotype.Service;

/**
 * 具体的优先级队列使用
 * 子类只需要定义key名
 *
 * author:xuyaokun_kzx
 * date:2021/7/27
 * desc:
*/
@Service
public class JobRedisPriorityQueue extends RedisPriorityQueue<JobVO> {

    private String key = "pending-job-list";//待处理任务列表

    @Override
    public String getKey() {
        return key;
    }


}
