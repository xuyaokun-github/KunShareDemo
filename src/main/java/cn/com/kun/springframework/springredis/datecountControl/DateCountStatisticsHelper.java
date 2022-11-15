package cn.com.kun.springframework.springredis.datecountControl;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于Redis实现的N天M次方案
 *
 * Created by xuyaokun On 2022/1/19 10:30
 * @desc:
 */
@Component
public class DateCountStatisticsHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateCountStatisticsHelper.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${dateCountStatistics.defaultExpireDays:60}")
    private int defaultExpireDays;
    /**
     *
     * @param key 业务key,例如 固定业务前缀:商户ID:客户ID
     * @param hashKey 例如 20220119
     * @param addCount
     */
    public void add(String key, String hashKey, int addCount){

        redisTemplate.opsForHash().put(key, hashKey, String.valueOf(addCount));
    }

    public void remove(String key, String hashKey){
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 支持模糊删除（不支持集群模式，TODO 需要优化）
     * 当某个业务停止时，可以主动调这个方法清理内存
     *
     * 实验：优化前，150万个key,耗时39291ms 24893ms(删除过程占了9335ms)
     * 优化后：1659855ms,更慢了
     * @param matchKey
     */
    public void remove(String matchKey){

        long start = System.currentTimeMillis();
        Set<String> keys = (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            /*
                代码虽然只是调用一次scan方法，但是spring-data-redis已经对scan做了封装，
                这个scan结合cursor.hasNext会多次redis scan，最终拿到所有match的结果
             */
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match("*" + matchKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
                //优化,无需放置内存，直接删除（这种方法不可取，当循环较大的时候，这个会慢得比较明显）
//                connection.del(cursor.next());
            }
            return keysTmp;
        });
        //
        LOGGER.info("扫描key耗时：{}", System.currentTimeMillis() - start);
        LOGGER.info("开始删除key总数：{}", keys.size());

        if(keys.size() > 0){
            //假如一次性删除太多，也很容易超时，需要将大批量拆成多个小批量
//        redisTemplate.delete(keys);

            //串行删除能避免超时报错，但整体处理很慢
//        keys.forEach(key->{
//            redisTemplate.delete(key);
//        });

            List<String> keyList = new ArrayList<>(keys);
            List<List<String>> partitions = org.apache.commons.collections4.ListUtils.partition(keyList, 1000);
            //串行
//        partitions.forEach(part->{
//            redisTemplate.delete(part);//这里传入的一批key
//        });

            //并行流
            long start2 = System.currentTimeMillis();
            partitions.stream().parallel().forEach(part->{
                redisTemplate.delete(part);
            });
            LOGGER.info("删除key总耗时：{}", System.currentTimeMillis() - start2);
        }

        LOGGER.info("整个删除过程总耗时：{}", System.currentTimeMillis() - start);


    }

    public Map<Object,Object> get(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取两个日期内的总数
     * @param key
     * @param date
     * @param date2
     * @return
     */
    public int get(String key, String date, String date2){

        AtomicInteger count = new AtomicInteger();
        try{
            int dateInt = Integer.parseInt(date);
            int date2Int = Integer.parseInt(date2);
            Map<String, String> map = redisTemplate.opsForHash().entries(key);
            Map<String, String> expireMap = new HashMap<>();
            //所有key都是八位date
            map.forEach((dateStrKey, value)->{
                int currentDateInt = Integer.parseInt(dateStrKey);
                if (dateInt <= currentDateInt && currentDateInt <= date2Int){
                    count.addAndGet(Integer.parseInt(value));
                }else {
                    //判断是否过期，假如过期了，则放入过期集合，统一删除
                    if (isExpire(currentDateInt)){
                        expireMap.put(key, dateStrKey);
                    }
                }
            });
            //清除过期key
            clearExpireData(expireMap);
        } catch(Exception e){
            LOGGER.error("DateCountStatisticsHelper查询异常", e);
           throw e;
        }
        return count.get();
    }

    private void clearExpireData(Map<String, String> expireMap) {
        if (expireMap.size() > 0){
            expireMap.forEach((key,hashKey)->{
                remove(key, hashKey);
            });
            LOGGER.info("DateCountStatisticsHelper本次清除过期数据：{}", JacksonUtils.toJSONString(expireMap));
        }
    }

    /**
     * 是否过期
     * @param dateInt
     * @return
     */
    private boolean isExpire(int dateInt) {

        String currentDate = DateUtils.toStr(new Date(), "yyyyMMdd");
        //当前天数，计算天数
        return DateUtils.betweenDays(String.valueOf(dateInt), currentDate) > defaultExpireDays;
    }
}
