package cn.com.kun.foo.consistentHash;

import java.util.*;

/**
 * 带虚拟节点的一致性哈希
 *
 */
public class ConsistentHashingWithVirtualNode {

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
     * 真实集群列表
     */
    private static List<String> realGroups = new LinkedList<>();

    /**
     * 虚拟节点映射关系
     */
    private static SortedMap<Integer, String> virtualNodes = new TreeMap<>();

    private static final int VIRTUAL_NODE_NUM = 1000;//每一个真实节点将派生出多少个虚拟节点

    static {
        // 先添加真实节点列表
        realGroups.addAll(Arrays.asList(groups));

        // 将虚拟节点映射到Hash环上
        for (String realGroup: realGroups) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                //生成虚拟节点的名字
                String virtualNodeName = getVirtualNodeName(realGroup, i);
                int hash = HashUtil.getHash(virtualNodeName);
                System.out.println("[" + virtualNodeName + "] launched @ " + hash);
                //放入哈希环
                /**
                 * 其实带不带虚拟节点，在算法实现上就只有这点区别
                 * 带虚拟节点的一致性哈希，只需要把虚拟节点放入环中
                 * 不带虚拟节点的一致性哈希则是把真实节点放入环中
                 */
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }

    /**
     * 生成虚拟节点的名字（memcached中也是这样做的，方便，直接拼接一点标识字符串就可以）
     * @param realName
     * @param num
     * @return
     */
    private static String getVirtualNodeName(String realName, int num) {
        return realName + "&&VN" + String.valueOf(num);
    }

    /**
     * 从虚拟节点名字中解析出真实节点
     * @param virtualName
     * @return
     */
    private static String getRealNodeName(String virtualName) {
        return virtualName.split("&&")[0];
    }

    /**
     * 根据随机数从环中找到目标节点
     * @param widgetKey
     * @return
     */
    private static String getServer(String widgetKey) {
        int hash = HashUtil.getHash(widgetKey);
        // 只取出所有大于该hash值的部分而不必遍历整个Tree
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        String virtualNodeName;
        if (subMap == null || subMap.isEmpty()) {
            // hash值在最尾部，应该映射到第一个group上
            virtualNodeName = virtualNodes.get(virtualNodes.firstKey());
        }else {
            virtualNodeName = subMap.get(subMap.firstKey());
        }
        //因为获取到的是虚拟节点名字，所以还需要解析成真实节点名
        return getRealNodeName(virtualNodeName);
    }

    /**
     * 节点发生增减，重新刷新哈希环
     *
     */
    private static void refreshHashCircle() {
        // 当集群变动时，刷新hash环，其余的集群在hash环上的位置不会发生变动
        virtualNodes.clear();
        for (String realGroup: realGroups) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                String virtualNodeName = getVirtualNodeName(realGroup, i);
                int hash = HashUtil.getHash(virtualNodeName);
                System.out.println("[" + virtualNodeName + "] launched @ " + hash);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }

    //添加新节点
    private static void addGroup(String identifier) {
        realGroups.add(identifier);
        refreshHashCircle();
    }

    //移除节点
    private static void removeGroup(String identifier) {
        int i = 0;
        for (String group:realGroups) {
            if (group.equals(identifier)) {
                realGroups.remove(i);
            }
            i++;
        }
        refreshHashCircle();
    }


    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        // 生成随机数进行测试
        Map<String, Integer> resMap = new HashMap<>();

        for (int i = 0; i < 100000; i++) {
            Integer widgetId = i;
            String group = getServer(widgetId.toString());
            if (resMap.containsKey(group)) {
                resMap.put(group, resMap.get(group) + 1);
            } else {
                resMap.put(group, 1);
            }
        }

        resMap.forEach(
                (k, v) -> {
                    System.out.println("group " + k + ": " + v + "(" + v/100000.0D +"%)");
                }
        );
    }


}
