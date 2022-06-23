package cn.com.kun.service.mybatis;

import cn.com.kun.bean.entity.DeadLockDemoDO;

public interface DeadLockDemoService {

    void insert(DeadLockDemoDO deadLockDemoDO);

    void deleteAll();
}
