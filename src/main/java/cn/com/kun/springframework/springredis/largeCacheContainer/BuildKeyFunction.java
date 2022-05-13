package cn.com.kun.springframework.springredis.largeCacheContainer;

@FunctionalInterface
public interface BuildKeyFunction<T> {

    <T> String buildKey(T souceObj);
}
