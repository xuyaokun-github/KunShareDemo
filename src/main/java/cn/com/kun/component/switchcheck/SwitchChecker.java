package cn.com.kun.component.switchcheck;

import org.springframework.util.Assert;

/**
 * 开关检查器
 * author:xuyaokun_kzx
 * date:2022/8/4
 * desc:
*/
public class SwitchChecker {

    private ISwitchFetcher switchFetcher;

    public SwitchChecker(ISwitchFetcher switchFetcher) {
        this.switchFetcher = switchFetcher;
    }

    /**
     * 是否开启
     * @return
     */
    public boolean isOpen(String switchName, boolean defaultFlag){
        Assert.notNull(switchFetcher, "开关查询器为空，请检查SwitchChecker定义");
        return switchFetcher.fetch(switchName, defaultFlag);
    }

    public boolean isClose(String switchName, boolean defaultFlag){
        Assert.notNull(switchFetcher, "开关查询器为空，请检查SwitchChecker定义");
        return !switchFetcher.fetch(switchName, defaultFlag);
    }


}
