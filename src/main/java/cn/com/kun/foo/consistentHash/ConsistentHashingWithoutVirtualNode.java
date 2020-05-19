package cn.com.kun.foo.consistentHash;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 没有虚拟节点的一致性哈希实现
 * 没有虚拟节点，会导致数据倾斜，某个节点聚集了大批数据
 */
public class ConsistentHashingWithoutVirtualNode {

    /**
     * 集群地址列表
     */
    private static String[] groups = {
            "192.168.0.0:111",
            "192.168.0.1:111",
            "192.168.0.2:111",
            "192.168.0.3:111",
            "192.168.0.4:111"
    };

    /**
     * 用于保存Hash环上的节点
     * 为什么要用TreeMap?因为树的结构有利于查询，读多写少的场景，适合用树来保存
     * 另外TreeMap支持排序，下面有用到它排序的特性，因为哈希环中每一个哈希值都是按顺序排列的
     */
    private static SortedMap<Integer, String> sortedMap = new TreeMap<>();

    /**
     * 初始化，将所有的服务器加入Hash环中
     */
    static {
        // 使用红黑树（TreeMap的底层是红黑树）实现，插入效率比较差，但是查找效率极高
        for (String group : groups) {
            //遍历所有真实的节点，每个节点都能得到一个哈希值
            int hash = HashUtil.getHash(group);
            System.out.println("[" + group + "] launched @ " + hash);
            //放入环中，用treemap的结构来充当环
            sortedMap.put(hash, group);
        }
    }

    /**
     * 计算对应的widget加载在哪个group上
     * （这个方法主要做的就是从环中找到目标节点）
     * @param widgetKey
     * @return
     */
    private static String getServer(String widgetKey) {
        //首先根据随机数，算出一个哈希值，根据这个哈希值去找到节点，如何找？
        int hash = HashUtil.getHash(widgetKey);
        //根据一致性哈希值的设计，环上有许多哈希值，只取等于这个哈希值和大于这个哈希值的节点
        // 只取出所有大于该hash值的部分而不必遍历整个Tree
        /**
         * 因为TreeMap是有序的，所以能通过tailMap方法直接获取到大于等于的所有哈希值
         * tailMap方法的含义：a view of the portion of this map whose keys are greater
         *         than or equal to {@code fromKey}
         */
        SortedMap<Integer, String> subMap = sortedMap.tailMap(hash);
        //假如通过这个哈希值获取不到，说明它比map中的所有哈希值都大，按照一致性哈希环的特性，只能取向后的节点，也就是第一个节点
        //因为环是一个环状的结构，最大的节点后就是第一个节点
        if (subMap == null || subMap.isEmpty()) {
            // hash值在最尾部，应该映射到第一个group上
            //sortedMap.firstKey()放回的是map中第一个key,也就是最小的key
            return sortedMap.get(sortedMap.firstKey());
        }
        //假如subMap不为空，subMap相当于是一个子集合，那就直接取第一个符合条件的即可。
        return subMap.get(subMap.firstKey());
    }


    /**
     * 通过这个测试方法，可以看到最终分配并不平均。
     * 于是有了后面用虚拟节点解决平均问题的方法
     * @param args
     */
    public static void main(String[] args) {
        // 生成随机数进行测试
        //Integer用来记录次数
        Map<String, Integer> resMap = new HashMap<>();

        for (int i = 0; i < 100000; i++) {
            //这是一个随机数，做负载均衡时根据这个随机数来决定要分发到哪个节点中
            Integer widgetId = (int)(Math.random() * 10000);
            //获取目标节点，具体逻辑就是在getServer方法里
            String server = getServer(widgetId.toString());
            //获取到目标节点，统计次数，以便后续看分配的比例是否平均
            if (resMap.containsKey(server)) {
                resMap.put(server, resMap.get(server) + 1);
            } else {
                resMap.put(server, 1);
            }
        }

        resMap.forEach(
                (k, v) -> {
                    System.out.println("group " + k + ": " + v + "(" + v/1000.0D +"%)");
                }
        );
    }
}
