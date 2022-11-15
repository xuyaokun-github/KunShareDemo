package cn.com.kun.springframework.springredis.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.*;

import java.util.*;

/**
 * Redis scan操作工具类(反例)
 * 支持集群模式
 *
 * author:xuyaokun_kzx
 * date:2022/5/19
 * desc:
*/
public class RedisScanErrorUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisScanErrorUtils.class);

    /**
     * 亲测成功(需要使用密码)
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

    /**
     * 该方法有bug,会陷入死循环（反例）
     * @param redisService
     * @param key
     * @return
     */
    public static List<String> getScan(Jedis redisService, String key) {

        List<String> list = new ArrayList<>();
        //扫描的参数对象创建与封装
        ScanParams params = new ScanParams();
        params.match(key);
        //扫描返回一百行，这里可以根据业务需求进行修改
        params.count(100);
        String cursor = "0";
        ScanResult scanResult = redisService.scan(cursor, params);

        //scan.getStringCursor() 存在 且不是 0 的时候，一直移动游标获取
        while (null != scanResult.getStringCursor()) {
            //封装扫描的结果
            list.addAll(scanResult.getResult());
            if (! "0".equals( scanResult.getStringCursor())) {
                scanResult = redisService.scan(cursor, params);
            } else {
                break;
            }
        }
        return list;
    }
}
