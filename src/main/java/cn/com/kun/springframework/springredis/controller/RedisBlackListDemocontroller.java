package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.springframework.springredis.service.RedisBlackListDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 写一个基于redis-bitmap实现黑名单的功能例子
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
@RequestMapping("/spring-redis-blacklist")
@RestController
public class RedisBlackListDemocontroller {

    public final static Logger LOGGER = LoggerFactory.getLogger(RedisBlackListDemocontroller.class);

    private String BLACKLIST_KEY = "user-blacklist";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisBlackListDemoService redisBlackListDemoService;

    /***
     * 添加到redis
     * @param id
     * @return
     */
    @RequestMapping(value = "/add")
    public String addBloomFilter(String id){
        try {
            redisBlackListDemoService.addByBloomFilter(id);
        }catch (Exception e){
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }

    @RequestMapping(value = "/addBatch")
    public String addBatch(String id){
        try {
            //添加一百个值到布隆过滤器，会占多少空间
            for (int i = 0; i < (10000 * 100); i++) {
                redisBlackListDemoService.addByBloomFilter(UUID.randomUUID().toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }

    /***
     * 从布隆过滤器中移除
     * @param id
     * @return
     */
    @RequestMapping(value = "/remove")
    public String removeByBloomFilter(String id){
        try {
            redisBlackListDemoService.removeByBloomFilter(id);
        }catch (Exception e){
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }

    /***
     * 查询是否存在
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/check")
    public boolean checkBloomFilter(String id){
        /**
         * 假如判断为不存在，那它肯定是不存在
         * 假如判断为存在，也有可能不存在！所以要进一步结合数据库的黑名单表判断
         */
        boolean b = redisBlackListDemoService.includeByBloomFilter(id);
        return b;
    }

    /***
     * 查询是否存在
     *
     * @return
     */
    @RequestMapping(value = "/checkIfExist")
    public boolean checkBloomFilter(){
        /**
         * 假如判断为不存在，那它肯定是不存在
         * 假如判断为存在，也有可能不存在！所以要进一步结合数据库的黑名单表判断
         */
        long start = System.currentTimeMillis();
        boolean b = redisBlackListDemoService.includeByBloomFilter(UUID.randomUUID().toString());
        LOGGER.info("耗时：{}ms", System.currentTimeMillis() - start);
        return b;
    }

    /**
     * 测试生成唯一的递增流水号
     * @param request
     * @return
     */
    @RequestMapping(value = "/testSetSmallOffset", method = RequestMethod.GET)
    public String testSetSmallOffset(HttpServletRequest request){

        //执行时返回的是之前是否存在，并不是指操作是否成功
        Boolean setSuccess = redisTemplate.opsForValue().setBit(BLACKLIST_KEY, 1, true);
        LOGGER.info("之前是否存在：{}", setSuccess);

        Boolean isExist = redisTemplate.opsForValue().getBit(BLACKLIST_KEY, 1);
        LOGGER.info("是否存在：{}", isExist);
        return "OK";
    }

    @RequestMapping(value = "/testSetBigOffset", method = RequestMethod.GET)
    public String testSetBigOffset(HttpServletRequest request){

        //
        Boolean setSuccess = redisTemplate.opsForValue().setBit(BLACKLIST_KEY, 4294967295L, true);
        LOGGER.info("之前是否存在：{}", setSuccess);

        Boolean isExist = redisTemplate.opsForValue().getBit(BLACKLIST_KEY, 4294967295L);
        LOGGER.info("是否存在：{}", isExist);

        return "OK";
    }

    @RequestMapping(value = "/testSetErrorBigOffset", method = RequestMethod.GET)
    public String testSetErrorBigOffset(HttpServletRequest request){

        //存入一个比4294967295L最大值还要大的偏移量，会直接抛异常
        Boolean setSuccess = redisTemplate.opsForValue().setBit(BLACKLIST_KEY, 4394967295L, true);
        LOGGER.info("存入结果：{}", setSuccess);

        Boolean isExist = redisTemplate.opsForValue().getBit(BLACKLIST_KEY, 4294967295L);
        LOGGER.info("是否存在：{}", isExist);

        return "OK";
    }

    @RequestMapping(value = "/addCacheForString")
    public String addCacheForString(){
        redisBlackListDemoService.addCacheForString();
        return "OK";
    }

    @RequestMapping(value = "/addCacheForHash")
    public String addCacheForHash(){
        redisBlackListDemoService.addCacheForHash();
        return "OK";
    }

    @RequestMapping(value = "/getCacheForHash")
    public String getCacheForHash(){
        redisBlackListDemoService.getCacheForHash();
        return "OK";
    }

}
