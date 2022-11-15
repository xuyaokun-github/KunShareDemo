package cn.com.kun.springframework.springredis.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Redis scan操作工具类
 * 仅支持集群模式
 *
 * author:xuyaokun_kzx
 * date:2022/5/19
 * desc:
*/
public class RedisClusterScanUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisClusterScanUtils.class);

    /**
     * 亲测成功(需要使用密码,不推荐使用)
     * 思路很简单，就是遍历集群中的所有节点，逐个节点处理
     * @param matchKey
     * @param redisTemplate
     * @return
     */
    public static Set<String> getRedisKeys(String matchKey, RedisTemplate redisTemplate, String password) {

        String keyPrefix = matchKey;
        Iterable<RedisClusterNode> redisClusterNodes = redisTemplate.getConnectionFactory().getClusterConnection().clusterGetNodes();
        Set<String> keySet = new HashSet<>();
        for (RedisClusterNode clusternode : redisClusterNodes) {
            if (clusternode.isMaster()) {
                String scanCusor = "0";
                List<String> scanResult = new ArrayList<>();
                do {
                    //不带密码
//                    JedisPool jedisPool = new JedisPool(clusternode.getHost(), clusternode.getPort());
                    //带密码方式
                    JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), clusternode.getHost(), clusternode.getPort(), 60000, password);
                    Jedis resource = jedisPool.getResource();
                    ScanResult<String> scan = resource.scan(scanCusor, new ScanParams().match(keyPrefix).count(1000));
                    scanCusor = scan.getStringCursor();
                    scanResult = scan.getResult();
                    scanResult.stream().forEach(item -> {
                        keySet.add(item);
                    });
                    resource.close();
                } while (!scanCusor.equals("0"));

            }
        }
        return keySet;
    }

    /**
     * 获取总数(底层实现必须是Jedis连接池)
     *
     * @param matchKey
     * @param redisTemplate
     * @return
     */
    public static Long getCount(String matchKey, RedisTemplate redisTemplate) {

        long start = System.currentTimeMillis();
        AtomicLong count = new AtomicLong(0);
        RedisClusterConnection redisClusterConnection = redisTemplate.getConnectionFactory().getClusterConnection();
        if (redisClusterConnection.getNativeConnection() instanceof JedisCluster){
            //获取Jedispool
            /*
                假如用的是lettuce，会报如下异常：
                java.lang.ClassCastException: io.lettuce.core.cluster.RedisAdvancedClusterAsyncCommandsImpl cannot be cast to redis.clients.jedis.JedisCluster
            */
            Map<String, JedisPool> clusterNodes = ((JedisCluster) redisClusterConnection.getNativeConnection()).getClusterNodes();
            for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
                //获取单个的jedis对象
                Jedis resource = entry.getValue().getResource();
                // 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)，此处要使用主节点
                // 从主节点获取数据
                if (!resource.info("replication").contains("role:slave")) {
                    //正确的方法
                    String scanCusor = "0";
                    List<String> scanResult = new ArrayList<>();
                    do {
                        ScanResult<String> scan = resource.scan(scanCusor, new ScanParams().match(matchKey).count(1000));
                        scanCusor = scan.getStringCursor();
                        scanResult = scan.getResult();
                        scanResult.stream().forEach(item -> {
                            count.incrementAndGet();
                        });
                    } while (!scanCusor.equals("0"));
                }
            }
        }else if (redisTemplate.getConnectionFactory() instanceof LettuceConnectionFactory){
            //假如用的是lettuce
            Iterable<RedisClusterNode> redisClusterNodes = redisClusterConnection.clusterGetNodes();
            Iterator<RedisClusterNode> iterator = redisClusterNodes.iterator();
            while (iterator.hasNext()) {
                RedisClusterNode next = iterator.next();
                //只采集master节点
                if (next.isMaster()){
                    Cursor<byte[]> scan = redisClusterConnection.scan(next, ScanOptions.scanOptions().match(matchKey).count(1000).build());
                    //这个写法有问题，会一直死循环
//                while (scan.hasNext()) {
//                    count.incrementAndGet();
//                }
                    //正确的写法
                    scan.forEachRemaining(new Consumer<byte[]>() {
                        @Override
                        public void accept(byte[] bytes) {
                            count.incrementAndGet();
                        }
                    });
                }
            }
        }

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
        RedisClusterConnection redisClusterConnection = redisTemplate.getConnectionFactory().getClusterConnection();

        //获取Jedispool
        Map<String, JedisPool> clusterNodes = ((JedisCluster) redisClusterConnection.getNativeConnection()).getClusterNodes();
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            //获取单个的jedis对象
            Jedis resource = entry.getValue().getResource();
            // 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)，此处要使用主节点
            // 从主节点获取数据
            if (!resource.info("replication").contains("role:slave")) {
                //正确的方法
                String scanCusor = "0";
                List<String> scanResult = new ArrayList<>();
                do {
                    ScanResult<String> scan = resource.scan(scanCusor, new ScanParams().match(matchKey).count(1000));
                    scanCusor = scan.getStringCursor();
                    scanResult = scan.getResult();
                    scanResult.stream().forEach(item -> {
                        list.add(item);
                    });
                } while (!scanCusor.equals("0"));

                //下面这句是有问题的，会陷入死循环
//                List<String> keys = getScan(jedis, matchKey);
            }
        }
        return list;
    }

}
