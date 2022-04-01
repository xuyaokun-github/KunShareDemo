package cn.com.kun.component.intervalCache.demo1;

@FunctionalInterface
public interface IntervalCacheDataLoader {

    <T> T loadData();

}
