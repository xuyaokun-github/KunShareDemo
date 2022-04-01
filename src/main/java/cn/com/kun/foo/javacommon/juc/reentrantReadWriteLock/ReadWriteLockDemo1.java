package cn.com.kun.foo.javacommon.juc.reentrantReadWriteLock;

import cn.com.kun.common.vo.ResultVo;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * 使用ReentrantReadWriteLock实现读写锁
 * 亲测成功,线程安全
 *
 * Created by xuyaokun On 2019/5/12 19:04
 * @desc:
 */
public class ReadWriteLockDemo1 {

    private static final Logger logger = Logger.getLogger(ReadWriteLockDemo1.class);

    private static Map<String, String> configMap = new ConcurrentHashMap<>();

    private final static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private final static Lock r = rwLock.readLock();
    private final static Lock w = rwLock.writeLock();

    /**
     * 加载系统配置
     *
     * @return
     */
    public static boolean load(){

        try{
            setConfigMap(loadData());
        } catch(Exception e){
            logger.error("加载系统配置表出现异常：", e);
            return false;
        }

        return true;
    }

    public static ResultVo reload(){

        Map<String, String> newConfigMap = null;
        try {
            w.lock();
            System.out.println(Thread.currentThread().getName() + "：准备开始更新缓存数据");
            System.out.println(Thread.currentThread().getName() + "---------------正式开始更新缓存数据--------------------------------------------");
            newConfigMap = loadData();
            setConfigMap(newConfigMap);
            System.out.println(Thread.currentThread().getName() + "---------------结束更新缓存数据--------------------------------------------");
            System.out.println(Thread.currentThread().getName() + "：更新缓存数据完毕！！！！！ ");
        } catch (Exception e) {
            logger.error("加载系统配置表出现异常：", e);
            return ResultVo.valueOfError("加载系统配置表出现异常");
        } finally {
            w.unlock();
            return ResultVo.valueOfSuccess(newConfigMap);
        }

//        if(isReloading.get()){
//            return new RetResult(400, "加载系统配置表正在进行中，请勿重复进行！！！");
//        }


    }

    private static Map<String, String> loadData(){

        //模拟从数据库查数
        Map<String, String> configMap = new HashMap<>();
        configMap.put("0", "aaa");
        configMap.put("1", "bbb");
        configMap.put("2", "ccc");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return configMap;
    }


    public static String get(String code) {

        if (configMap == null || configMap.isEmpty()){
            load();
        }

        String value = null;
        r.lock();
        System.out.println(Thread.currentThread().getName() + "：准备开始get");
        try{
            //重新加载时，要加锁锁住
            value = configMap.get(code);
            System.out.println(Thread.currentThread().getName() + "：获取到值：" + value);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            r.unlock();
        }
        return value;
    }

    public static int getInt(String code) {
        return Integer.parseInt(get(code));
    }

    public static long getLong(String code) {
        return Long.parseLong(get(code));
    }

    public static void setConfigMap(Map<String, String> configMap) {
        w.lock();
        ReadWriteLockDemo1.configMap = configMap;
        w.unlock();
    }



}
