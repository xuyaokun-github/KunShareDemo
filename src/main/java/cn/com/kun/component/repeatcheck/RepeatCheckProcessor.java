package cn.com.kun.component.repeatcheck;

public interface RepeatCheckProcessor {

    /**
     * 判断一个key是否存在
     * 假如存在说明重复了
     * @param key
     * @return
     */
    boolean checkRepeat(String key);

    /**
     * 添加一个唯一标识
     * 由子类实现，存redis还是数据库，自行选择
     * 这个方法可以是空实现
     * @param key
     * @return
     */
    boolean add(String key);


}
