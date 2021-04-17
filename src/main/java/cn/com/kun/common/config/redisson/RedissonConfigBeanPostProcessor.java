package cn.com.kun.common.config.redisson;


import cn.com.kun.common.utils.RedissonUtil;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;

/**
 * 自动注入Redisson对象-后置处理器
 * Created by xuyaokun On 2020/9/27 21:36
 * @desc:
 */
@Component
public class RedissonConfigBeanPostProcessor implements BeanPostProcessor {

//    @Autowired(required = false)
    private RedissonClient redissonClient;

    @PostConstruct
    private void init(){
        //可以放在@Configuration类中初始化，BeanPostProcessor这里可以直接注入使用
//        RedissonUtil.init(redissonClient);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class clazz = bean.getClass();

        //仅供服务层使用
        if (redissonClient != null &&
                (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class))){

            Field[] fields = clazz.getDeclaredFields();
            try{
                for (Field f : fields) {
                    if (f.isAnnotationPresent(RedissonAutowired.class) ){
                        f.setAccessible(true);
                        //根据bean的属性名生成默认的key名（其实可以根据业务角度来定义更具体的key名，做一个映射关系）
                        String keyName = genObjectKey(bean, f);
                        f.set(bean, RedissonUtil.getInjectObject(f.getType().getName(), keyName));
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }

        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    private String genObjectKey(Object bean, Field f){
        //属性名默认作为key的后缀
        return "KUNGHSU-" + bean.getClass().getName() + "-" + f.getName();
    }
}
