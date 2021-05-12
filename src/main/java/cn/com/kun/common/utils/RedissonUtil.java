package cn.com.kun.common.utils;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedissonUtil {

    private static RedissonClient redissonClient;
    private static final Logger logger = LoggerFactory.getLogger(RedissonUtil.class);

    public static final String NODE_ID = UUID.randomUUID().toString();
    public static final String HEARTBEAT_KEY_PREFIX = "HeartBeat-";//心跳key的前缀

    private static String LAST_NODE_ID;//上一次使用的node标识

    public static void init(RedissonClient redisson){
        if (redisson == null){
            return;
        }
        redissonClient = redisson;
        initLastNodeId();
        //启动心跳信号
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(()->{
            sendHeartBeat();
        }, 0 , 2, TimeUnit.MINUTES);
    }

    /**
     * 初始化方法，仅需在启动时执行一次
     */
    public static void initLastNodeId(){

        //读取springboot应用根目录下的node.txt文件
        try {
            String oldString = "";
            File file = new File("node.txt");
            if (file.exists()){
                oldString = FileUtils.readFileToString(file, "UTF-8");
                System.out.println(file.getAbsolutePath());
            }

            FileUtils.writeStringToFile(file, getCurrentNodeIdKey(), "UTF-8");
            if (file != null){
                System.out.println(file.getAbsolutePath());
                System.out.println(String.format("old:%s,new:%s", oldString, getCurrentNodeIdKey()));
            }
            LAST_NODE_ID = oldString;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static boolean delete(String keyName){
        return redissonClient.getBucket(keyName).delete();
    }

    /**
     * 设置字符串对象
     * @param keyName
     * @param value
     * @param timeToLive 过期时间，单位秒
     */
    public static void setString(String keyName, String value, long... timeToLive) {

        RBucket<String> rBucket = getRBucket(redissonClient, keyName);
        //同步放置
        if(timeToLive != null && timeToLive.length > 0){
            rBucket.set(value, timeToLive[0], TimeUnit.SECONDS);
        }else{
            rBucket.set(value);
        }

    }


    public static String getString(String keyName) {

        RBucket<String> bucket = getRBucket(redissonClient, keyName);
        return bucket.get();
    }

    /**
     * 获取字符串对象
     *
     * @param objectName
     * @return
     */
    private static <T> RBucket<T> getRBucket(RedissonClient redissonClient, String objectName) {
        RBucket<T> bucket = redissonClient.getBucket(objectName);
        return bucket;
    }

    /**
     * 设置自定义对象
     * @param keyName
     * @param value
     * @param timeToLive 过期时间，单位秒
     */
    public static <T> void setObject(String keyName, T value, long... timeToLive) {

        RBucket<T> rBucket = redissonClient.getBucket(keyName);
        // 同步放置
        if (timeToLive.length > 0){
            rBucket.set(value, timeToLive[0], TimeUnit.SECONDS);
        }else {
            rBucket.set(value);

        }

    }

    /**
     *
     * @return
     */
    public static <T> T getObject(String keyName) {

        RBucket<T> bucket = getRBucket(redissonClient, keyName);
        return bucket.get();
    }


    /**
     * 设置过期时间
     * 因为几乎所有Ression的API对象，都继承了RExpirable 所以可以用父类来设置过期时间
     * @param object
     * @param timeToLive
     * @return
     */
    private static boolean setExpireTime(RExpirable object, long... timeToLive){

        boolean isExpireSuccess = false;
        if(timeToLive != null && timeToLive.length > 0){
            isExpireSuccess = object.expire(timeToLive[0], TimeUnit.SECONDS);
        }else{
            //清过期时间，表示永久存
            isExpireSuccess = object.clearExpire();
        }
        return isExpireSuccess;
    }


    public static Iterable<String> getKeysByPattern(String keyName) {

        RKeys keys = redissonClient.getKeys();
        Iterable<String> foundedKeys = keys.getKeysByPattern(keyName + '*');
        return foundedKeys;
    }

    /**
     * 获取锁
     *
     * @param objectName
     * @return
     */
    public static RLock getRLock(String objectName) {
        RLock rLock = redissonClient.getLock(objectName);
        return rLock;
    }

    /**
     * 获取闭锁
     *
     * @param objectName
     * @return
     */
    public static RCountDownLatch getRCountDownLatch(String objectName) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取二进制流
     *
     * @param objectName
     * @return
     */
    public static RBinaryStream getRBinaryStream(String objectName) {
        RBinaryStream rBinaryStream = redissonClient.getBinaryStream(objectName);
        return rBinaryStream;
    }

    /**
     * 获取原子数
     *
     * @param objectName
     * @return
     */
    public static RAtomicLong getRAtomicLong(String objectName) {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(objectName);
        return rAtomicLong;
    }


    /**
     * 获取消息的Topic
     *
     * @param objectName
     * @return
     */
    public static RTopic getRTopic(String objectName) {
        RTopic rTopic = redissonClient.getTopic(objectName);
        return rTopic;
    }

    /**
     * 布隆过滤器
     *
     * @param objectName
     * @return
     */
    public static <T> RBloomFilter getRBloomFilter(String objectName) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(objectName);
        return bloomFilter;
    }

    /**
     *
     * @param objectName
     * @param <T>
     * @return
     */
    public static <T> RHyperLogLog getRHyperLogLog(String objectName) {
        RHyperLogLog<T> hyperLogLog = redissonClient.getHyperLogLog(objectName);
        return hyperLogLog;
    }

    /**
     * 限流器
     *
     * @param objectName
     * @return
     */
    public static  RRateLimiter getRRateLimiter(String objectName) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(objectName);
        return rateLimiter;
    }

    /**
     * 发送心跳信号
     *
     */
    public static void sendHeartBeat(){
        try{
            setString(getCurrentNodeIdKey(), "" + System.currentTimeMillis(), 3 * 60);
//            logger.debug("发送心跳 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch(Exception e){
            logger.error("发送心跳信号出现异常", e);
        }
    }

    /**
     * 判断节点是否存活
     *
     * @param nodeIdKey
     * @return
     */
    public static boolean isHostAlive(String nodeIdKey){

        String value = getString(nodeIdKey);
        if (StringUtils.isEmpty(value) || LAST_NODE_ID.equals(value)){
            //心跳值为null 或者 心跳值等于当前节点上一次的节点标识，也判断为失活
            return false;
        }

        long sleepCount = 3 * 60 - (System.currentTimeMillis() - Long.parseLong(value));
        try {
            Thread.sleep(sleepCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return !StringUtils.isEmpty(value);
    }

    public static String getCurrentNodeIdKey(){

        return HEARTBEAT_KEY_PREFIX + "主机名" + "-" + NODE_ID;
    }

    /**
     * 获取存活节点数
     *
     * @return
     */
    public static int getAliveCount(){

        //获取存活节点数
        Iterable<String> strings = RedissonUtil.getKeysByPattern(HEARTBEAT_KEY_PREFIX);
        int aliveCount = 0;
        Iterator<String> iterator = strings.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()){
            builder.append(iterator.next() + "|");
            aliveCount++;
        }
        logger.info("当前存活节点数：" + aliveCount + " 节点列表：" + builder.toString());
        return aliveCount;
    }

    /**
     * 获取Map对象
     *
     * @param objectName
     * @return
     */
    public static <K, V> RMap<K, V> getRMap(String objectName) {
        RMap<K, V> map = redissonClient.getMap(objectName);
        return map;
    }

    public static <T> RBlockingQueue<T> getRBlockingQueue(String objectName){
        return redissonClient.getBlockingQueue(objectName);
    }

    public static <T> RDeque<T> getRDeque(String objectName){
        return redissonClient.getDeque(objectName);
    }

    public static Object getInjectObject(String clazzName, String keyName){

        if (clazzName.equals(RMap.class.getTypeName())){
            return getRMap(keyName);
        } else if(clazzName.equals(RBlockingQueue.class.getTypeName())){
            return getRBlockingQueue(keyName);
        } else if(clazzName.equals(RCountDownLatch.class.getTypeName())){
            return getRCountDownLatch(keyName);
        } else if(clazzName.equals(RLock.class.getTypeName())){
            return getRLock(keyName);
        }

        return null;
    }

}
