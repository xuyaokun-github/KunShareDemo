package cn.com.kun.component.repeatcheck.impl;

import cn.com.kun.component.repeatcheck.RepeatCheckProcessor;

public class RepeatCheckSupport implements RepeatCheckProcessor {

    @Override
    public boolean checkRepeat(String key) {
        return false;
    }

    @Override
    public boolean add(String key) {
        return true;
    }

}
