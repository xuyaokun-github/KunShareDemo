package cn.com.kun.component.intervalCache.demo2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现一个最近被使用缓存LRU
 *
 * @author Kunghsu
 * @datetime 2018年4月14日 下午10:13:46
 * @desc @param <K>
 * @desc @param <V>
 */
public class MyIntervalCacheLRUCache<K, V> extends LinkedHashMap<K, V> {

    private int cacheSize;//緩存隊列的長度 = （cacheSize -1）

    private ReentrantLock lock = new ReentrantLock();

    public MyIntervalCacheLRUCache(int cacheSize) {
        // 第一个参数初始化容量，第二哥参数负载因子 第三个迭代顺序
        // 要实现LRU 必须为true
        super(16, (float) 0.75, true);
        this.cacheSize = cacheSize;
    }

//    @Override
//    public V get(Object key) {
//        return super.get(key);
//    }
//
//    @Override
//    public V put(K key, V value) {
//        return super.put(key, value);
//    }

    @Override
    public boolean remove(Object key, Object value) {

        try{
            lock.lock();
            return super.remove(key, value);
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {

        try{
            lock.lock();
            return super.get(key);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }finally {
            lock.unlock();
        }

    }

    @Override
    public V put(K key, V value) {
        try{
            lock.lock();
            return super.put(key, value);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }finally {
            lock.unlock();
        }
    }


    /**
     * 这个方法只需要继承并实现，不需要我们调用
     * 这个方法本来是来自父类LinkedHashMap，但是在父类中这个方法默认是false，false表示默认是不做任何删除，
     * 不会根据顺序先后来移除元素，默认是不具备LRU性质。父类提供这个实现方法让我们自定义实现LRU逻辑
     */
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //（当前队列长度大于或者等于cacheSize时，进行移除）
        //看下源码就知道，这里先从链头开始删，越靠近链头，证明其是被越少使用的
        return size() > cacheSize;
    }

}
