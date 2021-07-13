package cn.com.kun.component.clusterlock.redislock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * Redis分布式锁
 *
 * Created by xuyaokun On 2019/5/5 21:06
 * @desc:
 */
@Component
public class RedisLockUtil {

    @Autowired
//    private StringRedisTemplate redisTemplate;
    private RedisTemplate redisTemplate;

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 加锁
     *
     * @param lockKey 每把锁都有它的业务标识
     * @param requestId 不同的线程加锁要加以区分，可以使用时间戳或者客户端ID
     * @param expireTime 锁的超时时间
     * @return
     */
    public boolean getLock(String lockKey, String requestId, int expireTime) {

        boolean success = false;
        if (expireTime > 0){
            //表示使用超时(下面这种用法假如expireTime传0，会抛异常)
            success = (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection ->
                    connection.set(lockKey.getBytes(), requestId.getBytes(), Expiration.from(expireTime, TimeUnit.SECONDS),
                            RedisStringCommands.SetOption.SET_IF_ABSENT));
        }else {
            //不限定超时，永久锁（这种情况下，必须保证解锁一定能解成功）
            success = (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection ->
                    connection.setNX(lockKey.getBytes(), requestId.getBytes()));
        }

        return success;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId
     * @return
     */
    public boolean releaseLock(String lockKey, String requestId) {

        //使用Lua脚本来保证解锁动作的原子性
        String scriptStr = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> script = new DefaultRedisScript(scriptStr, Long.class);

        List<String> keys = new ArrayList<>();
        keys.add(lockKey);

        Long result = (Long) redisTemplate.execute(script, new StringRedisSerializer(), new RedisSerializer<Long>() {
            private final Charset charset = Charset.forName("UTF8");

            @Override
            public byte[] serialize(Long aLong) throws SerializationException {
                return (aLong == null ? null : (aLong.toString()).getBytes(charset));
            }

            @Override
            public Long deserialize(byte[] bytes) throws SerializationException {
                return (bytes == null ? null : Long.parseLong(new String(bytes, charset)));
            }
        }, keys, requestId);

        if (RELEASE_SUCCESS.equals(result)) {
            //加锁成功就返回1，否则就输出0
            return true;
        }
        return false;
    }


}
