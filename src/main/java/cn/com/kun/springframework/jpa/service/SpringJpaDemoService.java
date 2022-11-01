package cn.com.kun.springframework.jpa.service;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.jpa.entity.UserEntity;
import cn.com.kun.springframework.jpa.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SpringJpaDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringJpaDemoService.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationContext context;

    @PostConstruct
    public void init(){

        /*

         */
        Map<String, PlatformTransactionManager> beanMap = context.getBeansOfType(PlatformTransactionManager.class);
        if (beanMap != null){
            LOGGER.info("容器里PlatformTransactionManager的bean个数：" + beanMap.size());
            beanMap.forEach((k,v)->{
                LOGGER.info("===============PlatformTransactionManager beanMap:" + k);
            });
        }
    }

    public ResultVo testSave(String s) {

        UserEntity userEntity = new UserEntity();
        userEntity.setName(UUID.randomUUID().toString());
        userEntity.setPhone(UUID.randomUUID().toString());
        userEntity.setAge(ThreadLocalRandom.current().nextInt(100));
        userEntity.setAddress(UUID.randomUUID().toString());
        userEntity.setCreateTime(new Date());
        userRepository.save(userEntity);

        return ResultVo.valueOfError("");
    }


    public ResultVo testDateNull(String s) {

        UserEntity userEntity = new UserEntity();
        userEntity.setName(UUID.randomUUID().toString());
        userEntity.setPhone(UUID.randomUUID().toString());
        userEntity.setAge(ThreadLocalRandom.current().nextInt(100));
        userEntity.setAddress(UUID.randomUUID().toString());
        userRepository.save(userEntity);

        return ResultVo.valueOfError("");
    }

    public ResultVo testQuery(String s) {

        Optional<UserEntity> res = userRepository.findById(8L);
        return ResultVo.valueOfSuccess(res.get());
    }

    public ResultVo testQueryByWhere(String s) {

        List<UserEntity> res = userRepository.findByPhoneAndAddress("10086", "shenzhen");
        return ResultVo.valueOfSuccess(res);
    }

    @Transactional
    public ResultVo testUpdate(String s) {
        int res = userRepository.updateUserByAddress("10087", "shenzhen");
        return ResultVo.valueOfSuccess(res);
    }

    @Transactional
    public ResultVo testDelete(String s) {
        int res = userRepository.deleteUserByAddress(6L);
        return ResultVo.valueOfSuccess(res);
    }
}
