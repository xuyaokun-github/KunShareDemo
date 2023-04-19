package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.redis.RedisTemplateHelper;
import cn.com.kun.springframework.springredis.counter.RedisCounterDemoService;
import cn.com.kun.springframework.springredis.service.RedisListDemoService;
import cn.com.kun.springframework.springredis.vo.JobVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuyaokun On 2020/10/14 22:56
 * @desc: 
 */
@RequestMapping("/spring-redis")
@RestController
public class SpringRedisDemocontroller {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRedisDemocontroller.class);

    private final String HASH_KEY = "kunghsu.hash";

    private final String HASH_KEY_PREFIX = "kunghsu.hash.prefix_";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private RedisListDemoService redisListDemoService;

    @Autowired
    private RedisCounterDemoService redisCounterDemoService;

    /**
     * 测试生成唯一的递增流水号
     * @param request
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                while (true){
                    System.out.println(generateUniqueId());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "" + i).start();
        }
        
        return "OK";
    }

    /**
     * 生成递增流水号（不要求连续）
     * @return
     */
    private String generateUniqueId(){
        //获取当前秒作为key
        String currentSecond = DateUtils.nowWithNoSymbol();
        RedisAtomicLong counter = new RedisAtomicLong(currentSecond, redisTemplate.getConnectionFactory());
        counter.expire(60, TimeUnit.SECONDS);//设置过期时间
        long number = counter.incrementAndGet();
        if (number > 999999){
            //限制位数（从自己系统的每秒下单数考虑）
            throw new RuntimeException("生成流水号失败！请稍后重试");
        }
        String id = currentSecond + fillString(number);
        return id;
    }

    /**
     * 填充字符串至6位
     */
    private String fillString(long number){
        String source = "" + number;
        while (source.length() < 6){
            source = "0" + source;
        }
        return source;
    }

    /**
     * 测试hash
     * @param request
     * @return
     */
    @RequestMapping(value = "/testHashSet", method = RequestMethod.GET)
    public String testHashSet(HttpServletRequest request){

        //放入一万个key
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForHash().put(HASH_KEY, HASH_KEY_PREFIX +i, "" + System.currentTimeMillis());
        }

        return "OK";
    }

    /**
     * 测试hash
     * @param request
     * @return
     */
    @RequestMapping(value = "/testHashGet", method = RequestMethod.GET)
    public String testHashGet(HttpServletRequest request){

        String res = (String) redisTemplate.opsForHash().get(HASH_KEY, HASH_KEY_PREFIX + 10);
        System.out.println("testHashGet获取到结果：" + res);
        return "OK";
    }

    /**
     * 测试hash
     * @param request
     * @return
     */
    @RequestMapping(value = "/testHashGet2", method = RequestMethod.GET)
    public String testHashGet2(HttpServletRequest request){

        Map<String, String> map = redisTemplate.opsForHash().entries(HASH_KEY);
        System.out.println("testHashGet2获取到结果：" + map.size());

        //存取10000个key
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            String res = map.get(HASH_KEY_PREFIX + i);
            System.out.println(res);
        }
        System.out.println("testHashGet2总耗时：" + (System.currentTimeMillis() - start));

        return "OK";
    }

    /**
     * 测试hash
     * @param request
     * @return
     */
    @RequestMapping(value = "/testHashGet3", method = RequestMethod.GET)
    public String testHashGet3(HttpServletRequest request){

        //存取10000个key
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            String res = (String) redisTemplate.opsForHash().get(HASH_KEY, HASH_KEY_PREFIX + i);
            System.out.println(res);
        }
        System.out.println("testHashGet3总耗时：" + (System.currentTimeMillis() - start));

        return "OK";
    }


    @RequestMapping(value = "/testString", method = RequestMethod.GET)
    public ResultVo testString(HttpServletRequest request){

//        User user = new User();
//        user.setUsername("kunghsu");
//        redisTemplateHelper.set("spring-redis-demo-testString", user, 6000);
//
//        Object res = redisTemplateHelper.get("spring-redis-demo-testString");

        //下面的代码在低版本jedis包中会有问题，不能存太大的值
        // (这个问题已经在高版本修复)
        long time = 2147483647999999L;
        LOGGER.info("long值：{}", time);
        redisTemplate.opsForValue().set("spring-redis-demo-testString", "123456", time, TimeUnit.MILLISECONDS);
//        redisTemplate.opsForValue().set("spring-redis-demo-testString", "123456", time/1000, TimeUnit.SECONDS);
//        redisTemplate.opsForValue().set("spring-redis-demo-testString", "123456", (time/1000)/60, TimeUnit.MINUTES);

        return ResultVo.valueOfSuccess();
    }

    /**
     * 字符串的双引号问题
     * @param request
     * @return
     */
    @RequestMapping(value = "/testString2", method = RequestMethod.GET)
    public ResultVo testString2(HttpServletRequest request){

        redisTemplateHelper.set("noticeMsg", "kunghsu");
        String res = (String) redisTemplateHelper.get("noticeMsg");
        LOGGER.info("===========res：{}", res);
        return ResultVo.valueOfSuccess();
    }

    /**
     * 过期时间调研
     * 如果用DEL, SET, GETSET会将key对应存储的值替换成新的，命令也会清除掉超时时间
     *
     * @return
     */
    @RequestMapping(value = "/testString3", method = RequestMethod.GET)
    public ResultVo testString3(){

        String key = "kunghsu_key";
        //每次实验前，先删key
        redisTemplate.delete(key);

        //1.设置一个字符串
        String first = DateUtils.now();
        LOGGER.info("===========初次设置：{}", first);
        redisTemplate.opsForValue().set(key, first, 600, TimeUnit.SECONDS);

        //2.设置第二次
        String second = DateUtils.now() + "-second";
//        redisTemplate.opsForValue().set(key, second);//(这个操作相当于设置一个无限制的时间)

        redisTemplate.delete(key);
        //用lua脚本实现(只更新值，不重置过期时间，继续保留原key的过期时间。假如已过期，则不做任何处理)
        String script = "local expiretime = redis.call('TTL', KEYS[1]); " +
                " if expiretime > 0 then redis.call('set', KEYS[1], ARGV[1]);redis.call('EXPIRE', KEYS[1], tonumber(expiretime)) else return 0 end"
                ;
        DefaultRedisScript<Object> defaultRedisScript = new DefaultRedisScript<>(script, Object.class);
        List<String> keys = Arrays.asList(key);
        Object execute = redisTemplate.execute(defaultRedisScript, keys, second);

        //3.
        LOGGER.info("===========查询：{}", redisTemplate.opsForValue().get(key));

        return ResultVo.valueOfSuccess();
    }

    @RequestMapping(value = "/testSetAdd", method = RequestMethod.GET)
    public ResultVo testSetAdd(HttpServletRequest request){

        for (int i = 0; i < (10000*50); i++) {
            redisTemplateHelper.sSet("spring-redis-demo-testSetAdd", UUID.randomUUID().toString());
        }
//        Object res = redisTemplateHelper.get("spring-redis-demo-testString");

        return ResultVo.valueOfSuccess("");
    }

    @RequestMapping(value = "/testSetGet", method = RequestMethod.GET)
    public ResultVo testSetGet(HttpServletRequest request){

        long start = System.currentTimeMillis();
        /**
         * 从一个100万大小的set中判断某个元素是否存在，耗时多少？
         */
        boolean res = redisTemplateHelper.sHasKey("spring-redis-demo-testSetAdd", UUID.randomUUID().toString());
        LOGGER.info("耗时：{}ms", System.currentTimeMillis() - start);

        return ResultVo.valueOfSuccess("");
    }

    @RequestMapping(value = "/testList", method = RequestMethod.GET)
    public ResultVo testList(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            JobVO jobVO = new JobVO();
            jobVO.setName("job" + i);
            jobVO.setPriority(i);
            redisListDemoService.add(jobVO);
        }

//        for (int i = 0; i < 3; i++) {
//            //pop三次
//            JobVO jobVO = redisListDemoService.popOne();
//            LOGGER.info(JacksonUtils.toJSONString(jobVO));
//        }
        return ResultVo.valueOfSuccess("");
    }

    @GetMapping(value = "/testListPop")
    public ResultVo testListPop(HttpServletRequest request){

        for (;;) {
            //pop三次
            JobVO jobVO = redisListDemoService.popOne();
            if (jobVO == null){
                break;
            }
            LOGGER.info(JacksonUtils.toJSONString(jobVO));
        }
        return ResultVo.valueOfSuccess("");
    }

    @GetMapping(value = "/testListPopMoreThread")
    public ResultVo testListPopMoreThread(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            //启动多个线程pop
            new Thread(()->{
                JobVO jobVO = redisListDemoService.popOne();
                LOGGER.info(JacksonUtils.toJSONString(jobVO));
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }

    @GetMapping(value = "/testListPopMoreAndDelete")
    public ResultVo testListPopMoreAndDelete(HttpServletRequest request){

//        List<JobVO> jobVOList = redisListDemoService.popMore(3);
//        LOGGER.info(JacksonUtils.toJSONString(jobVOList));

        //启动一个线程，不断塞任务
        new Thread(()->{
            int count = 0;
            while (true){
                    JobVO jobVO = new JobVO();
                    jobVO.setName("job" + count++);
                    redisListDemoService.add(jobVO);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        //启动三个线程，抢任务
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                while (true){
                    List<JobVO> jobVOList = redisListDemoService.popMore(3);
                    if (jobVOList != null && !jobVOList.isEmpty()){
                        LOGGER.info("{}弹到的内容：{}", Thread.currentThread().getName(), JacksonUtils.toJSONString(jobVOList));
                    }

                }
            },"get-thread-" + i).start();
        }
        
        
        return ResultVo.valueOfSuccess("");
    }

    @GetMapping(value = "/testListPopMoreAndDeleteByMoreThread")
    public ResultVo testListPopMoreAndDeleteByMoreThread(HttpServletRequest request){

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                //popMore这个方法内置锁逻辑
                List<JobVO> jobVOList = redisListDemoService.popMore(3);
                LOGGER.info(JacksonUtils.toJSONString(jobVOList));
            }).start();
        }
        return ResultVo.valueOfSuccess("");
    }


    @GetMapping(value = "/testRedisCounter")
    public ResultVo testRedisCounter(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                redisCounterDemoService.addRequest();
            }).start();
        }

//        开启多个线程添加

        return ResultVo.valueOfSuccess("");
    }


    @GetMapping(value = "/testCountInit")
    public ResultVo testCountInit(HttpServletRequest request){

//        String key = "" + System.currentTimeMillis();
        String key = "kunghsu-count";

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                //假如计数器的key不存在，默认就是从0开始
                redisCounterDemoService.count(key);
            }).start();
        }

        Integer res2 = (Integer) redisTemplate.opsForValue().get(key);
        LOGGER.info(JacksonUtils.toJSONString(res2));
//        Long res2 = Long.parseLong(JedisUtils.get(key));

        return ResultVo.valueOfSuccess(res2);
    }

    /**
     * 验证 batchGet操作和 普通Get操作的 性能差异
     * 单线程做实验
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/testBatchGet")
    public ResultVo testBatchGet(HttpServletRequest request){

        List<String> keyList = Arrays.asList("aaa", "bbb", "ccc");

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            stringList.add(UUID.randomUUID().toString());
        }

        long startTime = System.currentTimeMillis();
        stringList.forEach(str->{
            for (String key : keyList){
                redisTemplate.opsForValue().get(key);
            }
        });

        long endTime = System.currentTimeMillis();
        LOGGER.info("普通get操作耗时：{}ms", endTime - startTime);

        stringList.forEach(str->{
            redisTemplate.opsForValue().multiGet(keyList);
        });
        LOGGER.info("multiGet操作耗时：{}ms", System.currentTimeMillis() - endTime);

        return ResultVo.valueOfSuccess();
    }



    @GetMapping(value = "/testBatchGetMultiThread")
    public ResultVo testBatchGetMultiThread(HttpServletRequest request) throws InterruptedException {

        List<String> keyList = Arrays.asList("aaa", "bbb", "ccc");

        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            stringList.add(UUID.randomUUID().toString());
        }

        long startTime = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            CountDownLatch finalCountDownLatch1 = countDownLatch;
            new Thread(()->{
                stringList.forEach(str->{
                    for (String key : keyList){
                        redisTemplate.opsForValue().get(key);
                    }
                });
                finalCountDownLatch1.countDown();
            }).start();
        }

        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        LOGGER.info("普通get操作耗时：{}ms", endTime - startTime);

        countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            CountDownLatch finalCountDownLatch = countDownLatch;
            new Thread(()->{
                stringList.forEach(str->{
                    redisTemplate.opsForValue().multiGet(keyList);
                });
                finalCountDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        LOGGER.info("multiGet操作耗时：{}ms", System.currentTimeMillis() - endTime);

        return ResultVo.valueOfSuccess();
    }
}
