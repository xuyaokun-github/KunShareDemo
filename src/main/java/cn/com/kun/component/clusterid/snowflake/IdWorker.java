package cn.com.kun.component.clusterid.snowflake;

import cn.com.kun.common.utils.DateUtils;

import java.util.Date;

/**
 * 基于雪花算法的分布式ID
 * 已解决解决时钟回拨问题
 * TODO：
 * workerId生成问题
 *
 * author:xuyaokun_kzx
 * date:2021/7/13
 * desc:
*/
public class IdWorker {

    //因为二进制里第一个 bit 为如果是 1，那么都是负数，但是我们生成的 id 都是正数，
    //所以第一个 bit 统一都是 0。

    /**
     * 机器ID  2进制5位  32位减掉1位 31个
     */
    private long workerId;

    /**
     * 机房ID 2进制5位  32位减掉1位 31个
     */
    private long datacenterId;

    /**
     * 代表一毫秒内生成的多个id的最新序号  12位 4096 -1 = 4095 个
     * 主要用来记录，做递增
     */
    private long sequence;

    /**
     * 设置一个时间初始值
     * 大约可以用69年，为什么？
     * 总共41位来放时间戳，2199023255552（2^41）
     *
     * 这个时间初始值应该尽量离自己的系统上线时间靠近，这样能多用几年
     * 因为初始值越近，计算出来的差值越小，能尽量从0开始递增到 2199023255552，能最大程度用完这69年
     */
    private long twepoch = 1585644268888L;

    /**
     * 机器id位数
     * 5位的机器id
     */
    private long workerIdBits = 5L;

    /**
     * 机房ID位数
     * 5位的机房id
     */
    private long datacenterIdBits = 5L;

    /**
     * 序号的位数
     * 每毫秒内产生的id数： 2 的 12次方
     * 雪花算法最后那截就是序号，表示一毫秒内能生成 2的12次方个ID
     */
    private long sequenceBits = 12L;

    // 这个是二进制运算，就是5 bit最多只能有31个值，
    //也就是说机器id最多只能是32以内
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    //同理，就是5 bit最多只能有31个数字，机房id最多只能是32以内
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 12位
     */
    private long workerIdShift = sequenceBits;
    /**
     * 17
     */
    private long datacenterIdShift = sequenceBits + workerIdBits;
    /**
     * 22位
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 这个值是4095
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    //记录产生时间毫秒数，判断是否是同1毫秒
    private long lastTimestamp = -1L;


    private long maxWaitOffset;

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    public long getMaxWaitOffset() {
        return maxWaitOffset;
    }

    /**
     * 假如没设置，说明不需要自旋，一遇到时钟回拨问题就抛异常终止
     * @param maxWaitOffset
     */
    public void setMaxWaitOffset(long maxWaitOffset) {
        this.maxWaitOffset = maxWaitOffset;
    }

    /**
     *
     * @param workerId 节点ID
     * @param datacenterId
     */
    public IdWorker(long workerId, long datacenterId) {
        // 检查机房id和机器id是否超过31 不能小于0
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0",
                            maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = 1;
    }

    // 这个是核心方法，通过调用nextId()方法，
//    让当前这台机器上的snowflake算法程序生成一个全局唯一的id
    public synchronized long nextId() {

        //获取当前时间戳，单位是毫秒
        long timestamp = timeGen();

        checkClockBackward(timestamp);

        // 下面是说假设在同一个毫秒内，又发送了一个请求生成一个id
        // 这个时候就得把seqence序号给递增1，最多就是4096
        if (lastTimestamp == timestamp) {

            /*
               一个毫秒内最多只能有4096个数字,这是最高并发量，因为位数有限，
               无论你设置sequence多大，只能必须要保证始终就是在4096这个范围内
               假如sequence超过了4096这个范围，做与运算之后，得到的还是小于4096的值
             */
            //sequenceMask是4095，二进制就是12个1
            sequence = (sequence + 1) & sequenceMask;

            //当某一毫秒的时间内产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
            if (sequence == 0) {
                //拿到下一毫秒的long值
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //假如当前请求的当前时间与之前记录的不等，说明序号需要重新从0开始累计
            sequence = 0;
        }
        //记录最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;

        // 最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit区；将机房id左移放到5 bit区；
        // 将机器id左移放到5 bit区，这是另一个5位的区；将序号放最后12 bit区，总共加起来就（1+41+5+5+12=64位）
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型

        /*
            怎么拼的，用的是或操作 |
            其实就是四个值放一起，然后取所有位都是1的，最终就是1
            举个例子
            0 00000000000000000000000000000000000000111 00000 00000 000000000000
            0 00000000000000000000000000000000000000000 00001 00000 000000000000
            0 00000000000000000000000000000000000000000 00000 00001 000000000000
            0 00000000000000000000000000000000000000000 00000 00000 000000000001
            最终得到：
            0 00000000000000000000000000000000000000111 00001 00001 000000000001
         */

        /*
            为什么需要左移呢？因为要把对应的值移到到它所属的bit区中
            例如机房ID，要左移17位，机器ID则需要左移12位
         */

        // 将四部分拼起来，时间戳部分 | 数据中心部分 | 机器标识部分 | 序列号部分
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) | sequence;
    }

    /**
        时钟回拨问题
        假如发现系统当前时间比之前记录的时间戳lastTimestamp还小，说明时钟被回调了
        我们可以选择自旋等待，也可以抛异常结束
        自旋等待不会影响业务，稍微等待一下就能恢复正常。抛异常结束，可能会导致某一次请求失败，根据业务重要性程度选择
    */
    private void checkClockBackward(long timestamp) {
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            //判断是否差距太大，超过最大容忍落后间距，则抛异常，假如没超，挂起继续等待
            if (maxWaitOffset > 0 && offset < maxWaitOffset){
                //maxWaitOffset一般是设置毫秒级，设置太多，会阻塞线程太久，导致其他问题
                try {
                    wait(maxWaitOffset);
                    //唤醒后，再次判断
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        //假如还是存在落后，就抛异常结束
                        throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                System.err.printf("clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
                throw new RuntimeException(
                        String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                                lastTimestamp - timestamp));
            }
        }
    }

    /**
     * 取下一毫秒的时间戳
     * 当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，
     * 直到下一毫秒，系统继续产生ID
     *
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long timeGen() {
        //据说可以优化System.currentTimeMillis()
//     return SystemClock.now();
        return System.currentTimeMillis();
    }

    /**
     * 传入一个id,反解得到这个id对应的时间戳
     * @param id
     * @return
     */
    public long timeStamp(long id){
        return (id >> (timestampLeftShift)) + twepoch;
    }

    public static void main(String[] args) {


        long sequenceBits = 12L;
        //-1 左移 12位 是 -4096
        System.out.println(-1L << sequenceBits);//得到4096
        System.out.println(-1L ^ -4096L);//得到4095

        //为什么是69年？
        long a = 1L;
        System.out.println(a<<41);//2199023255552
        long b = a<<41;
        System.out.println(b/1000/60/60/24/365);//69

        long c = a<<42;
        System.out.println(c/1000/60/60/24/365);//139

        //构造ID生成器
        IdWorker worker = new IdWorker(1, 1);//
        for (int i = 0; i < 22; i++) {
            System.out.println(worker.nextId());
        }

        System.out.println("----------------------------------");
        long id = worker.nextId();
        System.out.println(id);
        System.out.println(worker.timeStamp(id));
        Date date2 = new Date(worker.timeStamp(id));
        System.out.println(DateUtils.toStr(date2, DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss_SSS));

    }


}
