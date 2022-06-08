package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springredis.RedisTemplateHelper;
import cn.com.kun.springframework.springredis.priorityQueue.JobRedisPriorityQueue;
import cn.com.kun.springframework.springredis.service.RedisJobPriorityQueueService;
import cn.com.kun.springframework.springredis.vo.JobVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/spring-redis-zset")
@RestController
public class RedisJobPriorityQueueController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisJobPriorityQueueController.class);

    @Autowired
    RedisJobPriorityQueueService redisJobPriorityQueueService;

    @Autowired
    RedisTemplateHelper redisTemplateHelper;

    @Autowired
    JobRedisPriorityQueue jobRedisPriorityQueue;

    @RequestMapping(value = "/add")
    public String add(){

        for (int i = 0; i < 10; i++) {
            JobVO jobVO = new JobVO();
            int random = ThreadLocalRandom.current().nextInt(100);
            jobVO.setName("job" + random);
            jobVO.setPriority(random);
            jobVO.setDesc("" + System.currentTimeMillis());
            redisJobPriorityQueueService.push(jobVO);
        }
        return "添加成功";
    }

    @RequestMapping(value = "/count")
    public String count(){

        Long count = redisJobPriorityQueueService.count();
        LOGGER.info("count:{}", count);
        return "添加成功";
    }

    /**
     * http://localhost:8080/kunsharedemo/spring-redis-zset/zsetReverseRange
     * @return
     */
    @RequestMapping(value = "/zsetReverseRange")
    public ResultVo zsetReverseRange(){

        //获取所有
        LOGGER.info("输出0 - -1");
        Set<String> result = redisTemplateHelper.zsetReverseRange("pending-job-list",0, -1);
        for (String str : result) {
            LOGGER.info("str:{}", str);
        }
        LOGGER.info("输出0 - 1");
        Set<String> result2 = redisTemplateHelper.zsetReverseRange("pending-job-list",0, 0);
        for (String str : result2) {
            LOGGER.info("str:{}", str);
        }
        return ResultVo.valueOfSuccess(result);
    }


    @RequestMapping(value = "/deleteAllZset")
    public ResultVo deleteAllZset(){

        //获取所有
        redisTemplateHelper.del("pending-job-list");
        return ResultVo.valueOfSuccess();
    }

    @RequestMapping(value = "/pop")
    public ResultVo pop(){

        //获取所有
        JobVO res = redisJobPriorityQueueService.pop();
        return ResultVo.valueOfSuccess(res);
    }

    /**
     * 测试多线程弹出
     * @return
     */
    @RequestMapping(value = "/muitlThreadPop")
    public ResultVo muitlThreadPop(){

        //开10个线程，分别执行弹出，看是否会弹出相同的元素
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    JobVO res = jobRedisPriorityQueue.pop(JobVO.class);
                    if (res != null){
                        LOGGER.info("线程：{} 弹出内容：{}", Thread.currentThread().getName(), JacksonUtils.toJSONString(res));
                    }
                }
            }).start();
        }
        return ResultVo.valueOfSuccess(null);
    }

    /**
     * 测试多线程读写（入队和弹出同时并发）
     * 实践成果，cn.com.kun.springframework.springredis.priorityQueue.RedisPriorityQueue确实是线程安全的
     *
     * @return
     */
    @GetMapping(value = "/muitlThreadPopAndPush")
    public ResultVo muitlThreadPopAndPush(){

        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                while (true){
                    JobVO jobVO = new JobVO();
                    jobVO.setName("job" + count.incrementAndGet());
                    jobRedisPriorityQueue.push(jobVO, jobVO.getPriority());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "Put-Thread-" + i).start();
        }

        //开10个线程，分别执行弹出，看是否会弹出相同的元素
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                while (true){
                    JobVO res = jobRedisPriorityQueue.pop(JobVO.class);
                    if (res != null){
                        LOGGER.info("线程：{} 弹出内容：{}", Thread.currentThread().getName(), JacksonUtils.toJSONString(res));
                    }
                }
            }, "Get-Thread-" + i).start();
        }
        return ResultVo.valueOfSuccess(null);
    }

    ///////////////////////////////////////////////////////

    @RequestMapping(value = "/add2")
    public String add2(){

        for (int i = 0; i < 10; i++) {
            JobVO jobVO = new JobVO();
            int random = ThreadLocalRandom.current().nextInt(100);
            jobVO.setName("job" + random);
            jobVO.setPriority(random);
            jobVO.setDesc("" + System.currentTimeMillis());
            jobRedisPriorityQueue.push(jobVO, jobVO.getPriority());
        }
        return "添加成功";
    }

    @RequestMapping(value = "/pop2")
    public ResultVo pop2(){

        //获取所有
        JobVO res = jobRedisPriorityQueue.pop(JobVO.class);
        return ResultVo.valueOfSuccess(res);
    }

    /**
     * 注意，不能存放属性完全一样的元素，会自动去重
     * @return
     */
    @GetMapping(value = "/testAddSameItem")
    public ResultVo testAddSameItem(){

        while (true){
            JobVO res = jobRedisPriorityQueue.pop(JobVO.class);
            if (res != null){
                LOGGER.info("线程：{} 弹出内容：{}", Thread.currentThread().getName(), JacksonUtils.toJSONString(res));
            }else {
                //单线程弹，假如没弹到，说明没抢到或者弹完了
                break;
            }
        }

        JobVO jobVO = new JobVO();
        jobVO.setName("job1");
        jobVO.setPriority(1);
        jobRedisPriorityQueue.push(jobVO, jobVO.getPriority());
        jobRedisPriorityQueue.push(jobVO, jobVO.getPriority());

        long count = jobRedisPriorityQueue.count();

        return ResultVo.valueOfSuccess(count);
    }


}
