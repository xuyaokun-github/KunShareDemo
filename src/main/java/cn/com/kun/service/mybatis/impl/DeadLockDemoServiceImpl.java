package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import cn.com.kun.bean.entity.Student;
import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.mapper.DeadLockDemoMapper;
import cn.com.kun.mapper.StudentMapper;
import cn.com.kun.service.mybatis.DeadLockDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeadLockDemoServiceImpl implements DeadLockDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeadLockDemoServiceImpl.class);

    @Autowired
    private DeadLockDemoMapper deadLockDemoMapper;

    @Autowired
    private StudentMapper studentMapper;

//    @Transactional(rollbackFor = Exception.class,isolation = Isolation.REPEATABLE_READ) //用可重复读，不会有死锁
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE) //用串行化，会出现死锁
    @Override
    public void insert(DeadLockDemoDO deadLockDemoDO) {

        //先select，后update就会出现死锁
        deadLockDemoMapper.select(deadLockDemoDO);
        deadLockDemoMapper.insert(deadLockDemoDO);
    }

    @Override
    public void deleteAll() {
        deadLockDemoMapper.deleteAll();
    }

    @Transactional
    @Override
    public void updateByIdCard(String finalIdCard, String finalIdCard2) {

        long start = System.currentTimeMillis();
        LOGGER.info("更新线程 更新数据：{}", finalIdCard2);
        int res = studentMapper.updateByIdCard(UUID.randomUUID().toString(), finalIdCard2);

        ThreadUtils.sleep(5000);

        LOGGER.info("更新线程 更新数据：{}", finalIdCard);
        int res2 = studentMapper.updateByIdCard(UUID.randomUUID().toString(), finalIdCard);

    }


    /**
     *
     *
     * @param student
     * @param student2
     */
    @Transactional
    @Override
    public void saveBatch(Student student, Student student2) {

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        List<Student> studentList2 = new ArrayList<>();
        studentList2.add(student2);
        LOGGER.info("插入线程 开始插入数据:{} ", student.getIdCard());
        for (int j = 0; j < 20; j++) {
            studentList.stream().parallel().forEach(obj -> {
                try {
                    int res = studentMapper.insert(obj);
                }catch (Exception e){
                    LOGGER.error("save异常", e);

                }
            });
        }

        ThreadUtils.sleep(5000);
        LOGGER.info("插入线程 开始插入数据:{} ", student2.getIdCard());
        for (int j = 0; j < 20; j++) {
            studentList.stream().parallel().forEach(obj -> {
                try {
                    int res = studentMapper.insert(obj);
                }catch (Exception e){
                    LOGGER.error("save异常", e);

                }
            });
        }

    }

}
