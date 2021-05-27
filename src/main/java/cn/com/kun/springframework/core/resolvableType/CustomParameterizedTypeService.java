package cn.com.kun.springframework.core.resolvableType;

/**
 * 这个接口定义了2个泛型参数
 * @param <T>
 */
public interface CustomParameterizedTypeService<T,R> {

    /*abstract*/ void show(T data, R res);
}
