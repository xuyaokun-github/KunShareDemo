package cn.com.kun.springframework.jpa.service;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.jpa.entity.UserEntity;
import cn.com.kun.springframework.jpa.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SpringJpaDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringJpaDemoService.class);

    @Autowired
    UserRepository userRepository;

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
