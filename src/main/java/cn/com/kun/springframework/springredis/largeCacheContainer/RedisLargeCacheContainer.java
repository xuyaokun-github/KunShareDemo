package cn.com.kun.springframework.springredis.largeCacheContainer;

import cn.com.kun.common.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 大缓存容器
 * author:xuyaokun_kzx
 * date:2022/4/28
 * desc:
 * 1.设置的时候应该支持过期时间，避免僵尸数据占用内存
 *
 * TODO
 * 1.加一个上限控制，防止业务增长，把系统Redis资源撑爆
 * 假设数据增长比较猛，需要在Redis扩容后再把上限调大
 *
 *
*/
public class RedisLargeCacheContainer<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisLargeCacheContainer.class);

    private RedisTemplate redisTemplate;

    private String bizKey;

    private String countKey;

    private BuildKeyFunction<T> buildKeyFunction;

    private Long expireTimes;

    private TimeUnit expireTimeUnit;

    public RedisLargeCacheContainer(RedisTemplate redisTemplate, String bizKey, BuildKeyFunction buildKeyFunction, Long expireTimes, TimeUnit expireTimeUnit) {
        this.redisTemplate = redisTemplate;
        this.bizKey = bizKey;
        this.buildKeyFunction = buildKeyFunction;
        this.countKey = bizKey + "-COUNT";
        this.expireTimes = expireTimes;
        this.expireTimeUnit = expireTimeUnit;
    }

    public <T> boolean save(T sourceObj) {

        Map<String, Object> map = JacksonUtils.toMap(JacksonUtils.toJSONString(sourceObj));
        String key = buildKeyFunction.buildKey(sourceObj);
        put(key, map);
        return true;
    }

    private void put(String key, Map<String, Object> map) {
        if (StringUtils.isNotEmpty(key)){
            redisTemplate.opsForHash().putAll(bizKey + ":" + key, map);
            //设置过期时间时，可以考虑生成一个随机的过期时间，防止雪崩一次性过期太多
            //不过假如有异步线程去刷新数据的过期时间，这里的随机生成过期时间不再必要
            redisTemplate.expire(key, expireTimes, expireTimeUnit);
            //计数器加一
            redisTemplate.opsForHyperLogLog().add(countKey, key);
        }
    }


    public long size() {

        return redisTemplate.opsForHyperLogLog().size(countKey);
    }

    public boolean update(T sourceObj) {

        String key = buildKeyFunction.buildKey(sourceObj);
        Map<String, Object> map = JacksonUtils.toMap(JacksonUtils.toJSONString(sourceObj));
        if (map != null && !map.isEmpty()){
            Map<String, Object> notNullMap = new HashMap<>();
            map.forEach((k, v)->{
                if (v != null){
                    notNullMap.put(k, v);
                }
            });
            put(key, notNullMap);
        }
        return true;
    }


    public T get(String queryKey, Class<T> tClass) {
        return JacksonUtils.toJavaObject(redisTemplate.opsForHash().entries(bizKey + ":" + queryKey), tClass);
    }

    public boolean clear() {

        long start = System.currentTimeMillis();
        Set<String> keys = (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            /*
                代码虽然只是调用一次scan方法，但是spring-data-redis已经对scan做了封装，
                这个scan结合cursor.hasNext会多次redis scan，最终拿到所有match的结果
             */
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(bizKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
                //优化,无需放置内存，直接删除（这种方法不可取，当循环较大的时候，这个会慢得比较明显）
//                connection.del(cursor.next());
            }
            return keysTmp;
        });

        LOGGER.info("扫描key总耗时：{}ms", System.currentTimeMillis() - start);
        LOGGER.info("即将删除key的总数：{}ms", keys.size());

        if (keys.size() > 0) {
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
            partitions.stream().parallel().forEach(part -> {
                redisTemplate.delete(part);
            });
            LOGGER.info("删除key总耗时：{}ms", System.currentTimeMillis() - start2);
        }

        LOGGER.info("整个删除过程总耗时：{}ms", System.currentTimeMillis() - start);

        //这种删除方式不可取
//        return redisTemplate.delete(bizKey + "*");
        return true;
    }

}
