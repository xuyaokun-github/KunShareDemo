package cn.com.kun.foo.javacommon.collections.list;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestListPartition {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 150*10000; i++) {
            list.add(i);
        }

        //第一种方法
        long start = System.currentTimeMillis();
        //这个操作，大概要350-450ms
        List<List<Integer>> partitions = com.google.common.collect.Lists.partition(list, 1000);
        System.out.println("cost:" + (System.currentTimeMillis() - start));
        //第二种方法
        start = System.currentTimeMillis();
        //apache工具类，大概要60ms
        partitions = org.apache.commons.collections4.ListUtils.partition(list, 1000);
        System.out.println("cost:" + (System.currentTimeMillis() - start));

        //用流式遍历，要44536ms
//        List<List<Integer>> partitions = partition(list, 1000);
        //用并行流遍历，要983ms
//        List<List<Integer>> partitions = partition3(list, 1000);
        //最简单的方式，耗时10ms以下
//        List<List<Integer>> partitions = partitionSimple(list, 1000);

//        for (List<Integer> partition : partitions) {
//            System.out.println(list);
//        }

        System.out.println(partitions.size());

        //
        start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger finalCount1 = count;
        partitions.forEach(part->{
            part.forEach(item->{
                item.intValue();
//                finalCount1.addAndGet(1);
            });
        });
        System.out.println("具体处理完cost:" + (System.currentTimeMillis() - start));
        System.out.println("count数值:" + count.get());

        start = System.currentTimeMillis();
        count = new AtomicInteger(0);
        AtomicInteger finalCount = count;
        partitions.stream().parallel().forEach(part->{
            part.forEach(item->{
                item.intValue();
//                finalCount.addAndGet(1);
            });
        });
        System.out.println("并行流具体处理完cost:" + (System.currentTimeMillis() - start));
        System.out.println("count数值:" + count.get());

    }


    // 使用流遍历操作
    private static <T> List<List<T>> partition(final List<T> list, final int size) {
        Integer limit = (list.size() + size - 1) / size;
        List<List<T>> mglist = new ArrayList<List<T>>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            mglist.add(list.stream().skip(i * size).limit(size).collect(Collectors.toList()));
        });
        return mglist;
    }

    // 使用并行流处理
    private static <T> List<List<T>> partition3(final List<T> list, final int size) {
        Integer limit = (list.size() + size - 1) / size;
        List<List<T>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel()
                .map(a -> list.stream().skip(a * size).limit(size).parallel().collect(Collectors.toList()))
                .collect(Collectors.toList());
        return splitList;
    }

    /**
     * 最简单的方式
     * @param list
     * @param size
     * @param <T>
     * @return
     */
    private static <T> List<List<T>> partitionSimple(final List<T> list, final int size) {
        List<List<T>> result = new ArrayList<List<T>>();
        final int listSize = list.size();
        for (int i = 0; i < listSize; i += size) {
            List<T> subList = null;
            if (i + size > listSize) {
                subList = list.subList(i, i + listSize - i);
            } else {
                subList = list.subList(i, i + size);
            }
            result.add(subList);
        }
        return result;
    }
}
