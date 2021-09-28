package cn.com.kun.foo.javacommon.collections.map;

import java.util.*;

/**
 * 来源一道阿里一面面试题
 * 基于AbstractMap实现一个map，满足如下功能：
 * 限制大小，超过1024后，淘汰最久没有访问的key,当淘汰时需要调用一个回调函数 callback1()
 * 自动超时功能，当该key一个小时内没有被访问就被淘汰掉，当淘汰时需要调用另一个回到函数 callback2()
 *
 * Created by xuyaokun On 2021/9/17 22:44
 * @desc:
 */
public class MyAliMap extends AbstractMap<String, Object>{

    //先选定用哪种数据结构
    private LRUCache<String, Object> map;

    private int timeoutMillis;//1小时 (60*60*1000)

    /**
     * 构造函数
     * 让用户自由决定上限和超时时间
     *
     * @param maxSize
     * @param timeoutMillis
     */
    public MyAliMap(int maxSize, int timeoutMillis) {
        this.map = new LRUCache<>(maxSize);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public synchronized Object get(Object key) {

        ValueWrapper res = (ValueWrapper) map.get(key);
        if (res == null){
            removeTimeoutItem();
            return null;
        }
        //访问过，更新该key的时间
        res.setPutTime(System.currentTimeMillis());
        //然后遍历所有key,找到超时的，移除
        //遍历这个map,然后根据时间戳进行判断，假如时间差大于1小时就移除
        removeTimeoutItem();
        return res;
    }

    /**
     * 这个函数要考虑性能问题
     */
    private void removeTimeoutItem() {

        //遍历map，判断每个key的时间
        //但每次都要判断，比较麻烦
        Map<String, ValueWrapper> needToRemoveMap = new HashMap<>();
        map.forEach((k, v)->{
            String key = k;
            //移除的时候，调用回调方法
            ValueWrapper res = (ValueWrapper) v;
            if ((System.currentTimeMillis() - res.getPutTime()) > timeoutMillis){
                needToRemoveMap.put(key, res);
            }
        });

        needToRemoveMap.forEach((key,res)->{
            //移除该value
            map.remove(key);
            if (res.getTimeoutCallBackFunction() != null){
                res.getTimeoutCallBackFunction().timeoutCallback(key, res.getValue());
            }
        });
    }


    @Override
    public synchronized Object put(String key, Object value) {

        //当前元素放到队头
        ValueWrapper valueWrapper = new ValueWrapper(value, System.currentTimeMillis());
        //因为这个map属性已经是支持lru,所以可直接放
        // 执行lru算法
        return map.put(key, valueWrapper);
    }


    //设置回调
    public void setLruCallback(String key, LruCallBackFunction callbackFunctionLru){
        ((ValueWrapper)map.get(key)).setLruCallBackFunction(callbackFunctionLru);
    }


    public void setTimeoutCallback(String key, TimeoutCallBackFunction timeoutCallBackFunction){
        ((ValueWrapper)map.get(key)).setTimeoutCallBackFunction(timeoutCallBackFunction);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public Object remove(Object key) {
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    @Override
    public Collection<Object> values() {
        return super.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {

        //map的forEach是先调用entrySet，所以在这个方法里要对过期key,进行一遍处理
        removeTimeoutItem();
        //这个方式必须实现的
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public static class LRUCache<K,V> extends LinkedHashMap<K,V> {

        private static final long serialVersionUID = 1L;
        protected int maxElements;

        public LRUCache(int maxSize) {
            /**
             * true表示开启顺序记录
             */
            super(maxSize, 0.75F, true);
            maxElements = maxSize;
        }


        protected boolean removeEldestEntry(Map.Entry eldest) {

            //假如长度大于给定的大小，就进行移除
            boolean needRemove = size() > maxElements;
            if (needRemove){
                //触发回调方法
                //eldest即即将要被移除的元素
                Object object = eldest.getValue();
                if (object instanceof ValueWrapper){
                    LruCallBackFunction function = ((ValueWrapper) object).getLruCallBackFunction();
                    if (function != null){
                        function.lruCallback((String) eldest.getKey(), eldest.getValue());
                    }
                }
            }

            return needRemove;
        }

    }


    /**
     * key的封装
     * Created by xuyaokun On 2021/9/17 22:59
     * @desc:
     */
    public static class ValueWrapper {

        private Object value;

        private long putTime;

        //最久未被使用时被淘汰的回调
        private LruCallBackFunction lruCallBackFunction;

        //过期的回调
        private TimeoutCallBackFunction timeoutCallBackFunction;


        public ValueWrapper(Object value, long putTime) {
            this.value = value;
            this.putTime = putTime;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public long getPutTime() {
            return putTime;
        }

        public void setPutTime(long putTime) {
            this.putTime = putTime;
        }

        public LruCallBackFunction getLruCallBackFunction() {
            return lruCallBackFunction;
        }

        public void setLruCallBackFunction(LruCallBackFunction lruCallBackFunction) {
            this.lruCallBackFunction = lruCallBackFunction;
        }

        public TimeoutCallBackFunction getTimeoutCallBackFunction() {
            return timeoutCallBackFunction;
        }

        public void setTimeoutCallBackFunction(TimeoutCallBackFunction timeoutCallBackFunction) {
            this.timeoutCallBackFunction = timeoutCallBackFunction;
        }
    }

    @FunctionalInterface
    public interface LruCallBackFunction {

        void lruCallback(String key, Object value);
    }

    @FunctionalInterface
    public interface TimeoutCallBackFunction {

        void timeoutCallback(String key, Object value);
    }


}
