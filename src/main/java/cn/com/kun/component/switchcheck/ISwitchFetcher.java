package cn.com.kun.component.switchcheck;

/**
 * 开关查询器接口
 * author:xuyaokun_kzx
 * date:2022/8/4
 * desc:
*/
public interface ISwitchFetcher {

    /**
     * 开关名
     * @param switchName
     * @return
     */
    boolean fetch(String switchName, boolean defaultFlag);
}
