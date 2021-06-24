package cn.com.kun.springframework.springredis.bloomFilter;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisBloomFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 根据给定的布隆过滤器添加值
     * @param bloomFilterHelper
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void  addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper,String key, T value){
        Preconditions.checkArgument(bloomFilterHelper != null ,"bloomFilterHelper 不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset){
//            System.out.println("key: "+ key + " "+"value :"+i);
            redisTemplate.opsForValue().setBit(key, i ,true);
        }
    }


    /**
     * 从布隆过滤器中移除
     * @param bloomFilterHelper
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void  removeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper,String key, T value){
        Preconditions.checkArgument(bloomFilterHelper != null ,"bloomFilterHelper 不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset){
            System.out.println("key: "+ key + " "+"value :"+i);
            redisTemplate.opsForValue().setBit(key, i ,false);
        }
    }

    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key , T value){
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("key : " + key + " " + "value : " + i);
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }
}