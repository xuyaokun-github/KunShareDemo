package cn.com.kun.springframework.batch;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 批处理执行情况统计器
 *
 * Created by xuyaokun On 2020/10/20 22:28
 * @desc:
 */
public class BatchExecCounter {

    private static final ThreadLocal<String> countIdThreadLocal = new ThreadLocal<>();//每个线程都有专用的countId

    private static final ConcurrentHashMap<String, CountData> countMap = new ConcurrentHashMap();//存放统计数据的集合

    /**
     * 初始化统计ID(job的一次执行拥有一个唯一ID)
     */
    public static void initCountId(){

        String countId = UUID.randomUUID().toString();
        if (null == countIdThreadLocal.get()){
            countIdThreadLocal.set(countId);
        }
        CountData countData = new CountData(new AtomicLong(0), new AtomicLong(0));
        countMap.put(countId, countData);
    }

    /**
     * 成功+1
     */
    public static void countSuccess(){
        String countId = countIdThreadLocal.get();
        CountData countData = countMap.get(countId);
        //加一
        countData.getSuccessNum().incrementAndGet();
    }

    /**
     * 失败+1
     */
    public static void countFail(){
        String countId = countIdThreadLocal.get();
        CountData countData = countMap.get(countId);
        //加一
        countData.getFailNum().incrementAndGet();
    }

    /**
     * 统计+1
     * @param isSuccess (支持传入成功或者失败标志)
     */
    public static void count(boolean isSuccess){
        if (isSuccess){
            countSuccess();
        }else {
            countFail();
        }
    }

    public static CountData getCountData(){
        String countId = countIdThreadLocal.get();
        CountData countData = countMap.get(countId);
        return countData;
    }

    /**
     * 移除统计ID
     */
    public static void removeCountId(){
        String countId = countIdThreadLocal.get();
        countMap.remove(countId);
        countIdThreadLocal.remove();
    }

    public static class CountData {

        private AtomicLong successNum;

        private AtomicLong failNum;

        private Map<String, String> otherInfo;

        public CountData(AtomicLong success, AtomicLong fail){
            successNum = success;
            failNum = fail;
        }

        public AtomicLong getSuccessNum() {
            return successNum;
        }

        public AtomicLong getFailNum() {
            return failNum;
        }

        public Map<String, String> getOtherInfo() {
            return otherInfo;
        }

        public void setOtherInfo(Map<String, String> otherInfo) {
            this.otherInfo = otherInfo;
        }
    }

}
