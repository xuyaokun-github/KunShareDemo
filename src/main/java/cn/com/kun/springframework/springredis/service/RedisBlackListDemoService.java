package cn.com.kun.springframework.springredis.service;

import cn.com.kun.bean.entity.User;
import cn.com.kun.springframework.springredis.bloomFilter.BloomFilterHelper;
import cn.com.kun.springframework.springredis.bloomFilter.RedisBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 实现黑名单的功能最终还是决定使用布隆过滤器
 *
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
@Service
public class RedisBlackListDemoService {

    private String BLACKLIST_KEY = "user-blacklist";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisBloomFilter redisBloomFilter;
    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    /**
     * 基于bitmap的实现非常有限
     * @param user
     */
    public void checkIfExist(User user){
        //只能接受long型offset,并且有最大值：4294967295 （42亿多）
        long offset = 1000000000000000000l;
        //583df666c6ffa2568b4db6d6fbd7c7a6
        redisTemplate.opsForValue().getBit(BLACKLIST_KEY, offset);
    }

    /**
     * 添加用户到黑名单后，就调这个方法操作redis,把用户的ID作为偏移量long值存进去
     * @param user
     */
    public void addToBlackList(User user){
        long offset = 1L;
        redisTemplate.opsForValue().setBit(BLACKLIST_KEY, offset, true);
    }

    /**
     * 添加进布隆过滤器
     * @param value
     */
    public void addByBloomFilter(String value) {

        redisBloomFilter.addByBloomFilter(bloomFilterHelper, "my-boomfilter", value);
    }

    public void removeByBloomFilter(String value) {

        redisBloomFilter.removeByBloomFilter(bloomFilterHelper, "my-boomfilter", value);
    }

    /**
     * 检查是否存在
     * @param value
     * @return
     */
    public boolean includeByBloomFilter(String value) {
        return redisBloomFilter.includeByBloomFilter(bloomFilterHelper,"my-boomfilter", value);
    }
}
