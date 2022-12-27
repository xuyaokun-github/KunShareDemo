package cn.com.kun.component.distributedId.snowflake;

import cn.com.kun.common.utils.DateUtils;

import java.util.Date;

public class IdWorkerTester {


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
