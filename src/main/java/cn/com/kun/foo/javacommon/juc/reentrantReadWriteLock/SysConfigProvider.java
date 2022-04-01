package cn.com.kun.foo.javacommon.juc.reentrantReadWriteLock;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * 使用AtomicBoolean实现读写锁
 * 这是一个经典的反例
 *
 * Created by xuyaokun On 2019/5/12 19:04
 * @desc:
 */
public class SysConfigProvider {

    private static final Logger logger = Logger.getLogger(SysConfigProvider.class);

    private static Map<String, String> configMap = new ConcurrentHashMap<>();

    private static AtomicBoolean isReloading = new AtomicBoolean(false);//是否在重新加载配置

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

        if(isReloading.get()){
            return ResultVo.valueOfError("加载系统配置表正在进行中，请勿重复进行！！！");
        }

        isReloading.set(true);
        System.out.println(Thread.currentThread().getName() + "---------------正式开始更新缓存数据--------------------------------------------");
        Map<String, String> newConfigMap = null;
        try{
            newConfigMap = loadData();
            setConfigMap(newConfigMap);
            System.out.println(Thread.currentThread().getName() + "---------------本次更新内容：-----" + JacksonUtils.toJSONString(newConfigMap));
        } catch(Exception e){
            logger.error("加载系统配置表出现异常：", e);
            return ResultVo.valueOfError("加载系统配置表出现异常");
        } finally {
            System.out.println(Thread.currentThread().getName() + "---------------结束更新缓存数据--------------------------------------------");
            isReloading.set(false);
        }
        return ResultVo.valueOfSuccess(newConfigMap);
    }

    private static Map<String, String> loadData(){

        //模拟从数据库查数
        Map<String, String> configMap = new HashMap<>();
        configMap.put("0", "aaa" + "-" + UUID.randomUUID().toString());
        configMap.put("1", "bbb" + "-" + UUID.randomUUID().toString());
        configMap.put("2", "ccc" + "-" + UUID.randomUUID().toString());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return configMap;
    }


    public static String get(String code) {

        for (;;){
            if (!isReloading.get()){
                if (configMap == null || configMap.isEmpty()){
                    load();
                }
                //重新加载时，要加锁锁住
                return configMap.get(code);
            }
        }

    }

    public static int getInt(String code) {
        return Integer.parseInt(get(code));
    }

    public static long getLong(String code) {
        return Long.parseLong(get(code));
    }

    public static void setConfigMap(Map<String, String> configMap) {
        isReloading.set(true);
        SysConfigProvider.configMap = configMap;
        isReloading.set(false);
    }



}
