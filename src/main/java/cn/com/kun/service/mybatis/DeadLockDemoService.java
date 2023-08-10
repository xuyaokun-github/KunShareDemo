package cn.com.kun.service.mybatis;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import cn.com.kun.bean.entity.Student;

public interface DeadLockDemoService {

    void insert(DeadLockDemoDO deadLockDemoDO);

    void deleteAll();

    void updateByIdCard(String finalIdCard, String finalIdCard2);

    void saveBatch(Student student, Student student2);
}
