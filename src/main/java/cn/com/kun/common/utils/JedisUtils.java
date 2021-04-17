package cn.com.kun.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 不推荐用Jedis(引入它是为了查问题)
 * Created by xuyaokun On 2020/9/16 22:31
 * @desc: 
 */
public class JedisUtils {

    private static JedisPool pool = null;

    public static void setPool(JedisPool jedisPool){
        pool = jedisPool;
    }

    public static void set(String key, String value){

        Jedis jedis = pool.getResource();
        jedis.set(key, value);
        //使用完之后必须归还连接，调用close是先判断是否来自池子，假如是就归还到池子中
        jedis.close();
        //官方不建议调用下面的方法，已经被弃用
//        pool.returnResource(jedis);
    }

    public static String get(String key){
        Jedis jedis = pool.getResource();
        String value = new String(jedis.get(key));
        jedis.close();
        return value;
    }


}

