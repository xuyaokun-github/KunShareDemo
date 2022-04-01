package cn.com.kun.component.intervalCache.demo1;


import java.util.concurrent.atomic.AtomicReference;

/**
 * 实现1
 * 亲测成功
 *
 * author:xuyaokun_kzx
 * date:2022/4/1
 * desc:
 */
public class IntervalCache1 {

    private AtomicReference<Object> atomicReference = new AtomicReference();

    private AtomicReference<Long> timeMillis = new AtomicReference();

    private Long interval = 1000 * 60L;

    private IntervalCacheDataLoader dataLoader;

    public IntervalCache1(Long interval, IntervalCacheDataLoader dataLoader) {
        this.interval = interval;
        this.dataLoader = dataLoader;
    }

    public void init(Long customInterval) {
        interval = customInterval;
    }

    public <T> void set(T obj) {

        //这里先更新时间，再设置新值（避免多线程check的时候将新值判断为旧值）
        timeMillis.set(System.currentTimeMillis());
        atomicReference.set(obj);
    }

    /**
     * 检查间隔时间是否到了
     * 假如时间间隔到了，调用获取数据方法，重新设置
     *
     * @return
     */
    public boolean check() {

        long now = System.currentTimeMillis();
        if (timeMillis.get() == null || (now - timeMillis.get() > interval)) {
            //超过间隔了，可以重新检测了
            set(load());
            System.out.println(Thread.currentThread().getName() + "重新设值完毕");
            return true;
        } else {
            //无需更新，继续用旧值

        }
        return false;
    }

    private <T> T load() {

        return dataLoader.loadData();
    }

    public <T> T get() {
        return (T) atomicReference.get();
    }

}
