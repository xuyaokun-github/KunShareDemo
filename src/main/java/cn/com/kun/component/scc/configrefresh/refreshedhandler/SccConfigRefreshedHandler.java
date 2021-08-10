package cn.com.kun.component.scc.configrefresh.refreshedhandler;

import java.util.Collection;

/**
 * 为什么需要这个类？
 * 因为EnvironmentChangeEvent事件响应时，容器里的RefreshScope注解定义的bean还没刷新，所以需要更靠后的阶段处理
 *
 * author:xuyaokun_kzx
 * date:2021/8/9
 * desc:
*/
public interface SccConfigRefreshedHandler {

    /**
     * 刷新后的处理
     * @param refreshedKeyList
     */
    void refreshed(Collection<String> refreshedKeyList);

    /**
     * 默认方法--判断是否有关心的key
     *
     * @param refreshedKeyList
     * @param keyPrefix
     * @return
     */
    default boolean hasConcernedKey(Collection<String> refreshedKeyList, String keyPrefix) {
        for (String key : refreshedKeyList) {
            if (key.startsWith(keyPrefix)){
                return true;
            }
        }
        return false;
    }
}
