package cn.com.kun.springframework.springredis.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Redis scan操作工具类(只支持springboot2.1以上)
 * 同时支持单节点模式&集群模式
 * 与RedisTemplate配合使用
 *
 * author:xuyaokun_kzx
 * date:2022/5/19
 * desc:
*/
public class RedisScanUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisScanUtils.class);

    /**
     * 获取总数(底层实现必须是Jedis连接池???)
     *
     * @param matchKey
     * @param redisTemplate
     * @return
     */
    public static Long getCount(String matchKey, RedisTemplate redisTemplate) {

        long start = System.currentTimeMillis();
        AtomicLong count = new AtomicLong(0);
        scan(matchKey, redisTemplate, key -> {
            count.incrementAndGet();
        });
        LOGGER.info("scan操作统计到key总数：{} 耗时：{}ms", count.get(), (System.currentTimeMillis() - start));
        return count.get();
    }


    /**
     * 支持模糊删除（支持集群模式）
     *
     * @param matchKey
     */
    public static void remove(String matchKey, RedisTemplate redisTemplate){

        long start = System.currentTimeMillis();
        Set<String> keys = new HashSet<>(getRedisKeys(matchKey, redisTemplate));
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



    /**
     * 获取所有key
     * 亲测也是奏效的（用这个方法，就不需要传入密码）
     *
     * @param matchKey
     * @param redisTemplate
     * @return
     */
    public static List<String> getRedisKeys(String matchKey, RedisTemplate redisTemplate) {

        List<String> list = new ArrayList<>();
        scan(matchKey, redisTemplate, key ->{
            list.add(key);
        });
        return list;
    }


    private static void scan(String matchKey, RedisTemplate redisTemplate, ScanKeyHandler scanHandler){


        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection connection = null;
        try {
            connection = connectionFactory.getConnection();
            if (connection instanceof RedisClusterConnection){
                //集群模式
                /*
                    假如连接池是Jedis,拿到的实现是org.springframework.data.redis.connection.jedis.JedisClusterConnection
                    假如连接池是Lettuce,拿到的实现是org.springframework.data.redis.connection.lettuce.LettuceClusterConnection
                 */
                RedisClusterConnection redisClusterConnection = ((RedisClusterConnection)connection);
                Iterable<RedisClusterNode> redisClusterNodes = redisClusterConnection.clusterGetNodes();
                Iterator<RedisClusterNode> iterator = redisClusterNodes.iterator();
                while (iterator.hasNext()) {
                    RedisClusterNode next = iterator.next();
                    //只采集master节点
                    if (next.isMaster()){
                        //这个api,在1.5.X版本不支持，自2.1版本开始引入
                        Cursor<byte[]> scan = redisClusterConnection.scan(next, ScanOptions.scanOptions().match(matchKey).count(1000).build());
                        //这个写法有问题，会一直死循环
//                while (scan.hasNext()) {
//                    count.incrementAndGet();
//                }
                        //正确的写法
                        scan.forEachRemaining(new Consumer<byte[]>() {
                            @Override
                            public void accept(byte[] bytes) {
                                scanHandler.apply(new String(bytes));
                            }
                        });
                    }
                }
            }else {
                //单节点模式
                ScanOptions scanOptions = ScanOptions.scanOptions().count(1000L).match(matchKey).build();
                Cursor<byte[]> cursors = connection.scan(scanOptions);
                cursors.forEachRemaining(new Consumer<byte[]>() {
                    @Override
                    public void accept(byte[] bytes) {
                        scanHandler.apply(new String(bytes));
                    }
                });
            }

        } finally {
            RedisConnectionUtils.releaseConnection(connection, connectionFactory);
        }

    }

    @FunctionalInterface
    private interface ScanKeyHandler {

        void apply(String key);
    }


}
